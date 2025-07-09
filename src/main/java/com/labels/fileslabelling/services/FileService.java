package com.labels.fileslabelling.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.models.*;
import com.azure.core.util.polling.SyncPoller;
import com.labels.fileslabelling.models.*;
import com.labels.fileslabelling.models.BoundingRegion;
import com.labels.fileslabelling.models.DocumentPage;
import com.labels.fileslabelling.models.DocumentTable;
import com.labels.fileslabelling.models.DocumentTableCell;
import com.labels.fileslabelling.models.DocumentWord;
import com.labels.fileslabelling.repositories.FileDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service central pour le traitement des fichiers :
 *   • Upload vers Azure Blob Storage
 *   • Analyse via Azure Document Intelligence (OCR, tables, paires clé-valeur)
 *   • Transformation des résultats SDK en nos modèles métier
 *   • Persistance dans Cosmos DB via FileDocumentRepository
 */
@Service
public class FileService {

    private final BlobContainerClient blobContainerClient;
    private final FileDocumentRepository docRepo;
    private final DocumentIntelligenceClient docClient;

    public FileService(BlobContainerClient blobContainerClient,
                       FileDocumentRepository docRepo,
                       DocumentIntelligenceClient docClient) {
        this.blobContainerClient = blobContainerClient;
        this.docRepo = docRepo;
        this.docClient = docClient;
    }

    /**
     * Polling simple pour s'assurer que le blob est disponible
     * avant de récupérer son URL ou d'y lancer une analyse.
     */
    private void waitForBlobAvailability(BlobClient blob) throws InterruptedException {
        int maxRetries = 10, retries = 0;
        while (retries < maxRetries && !blob.exists()) {
            Thread.sleep(500);
            retries++;
        }
        // Si nécessaire, on pourrait lever une exception après maxRetries ici.
    }

    /** Récupère tous les documents stockés. */
    public List<FileDocument> findAll() {
        return docRepo.findAll();
    }

    /**
     * Récupère un document par ID.
     *
     * @param id identifiant Cosmos DB
     * @return Optional vide si non trouvé
     */
    public Optional<FileDocument> findById(String id) {
        return docRepo.findById(id);
    }

    /**
     * 1) Upload du fichier dans Azure Blob Storage
     * 2) Lancement d'une analyse « layout + key-value pairs + tables »
     * 3) Mapping des pages, mots, paires clé-valeur et tables
     * 4) Persistance du résultat dans Cosmos DB avec statut IN_REVIEW
     *
     * @param file MultipartFile reçu du contrôleur
     * @return FileDocument enrichi et sauvegardé
     */
    public FileDocument uploadAndAnalyze(MultipartFile file) throws IOException, InterruptedException {
        // --- Upload ---
        String blobName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobClient blob = blobContainerClient.getBlobClient(blobName);
        blob.upload(file.getInputStream(), file.getSize(), true);
        waitForBlobAvailability(blob);
        String url = blob.getBlobUrl();

        // --- Analyse de document ---
        AnalyzeDocumentOptions options = new AnalyzeDocumentOptions(url)
                .setDocumentAnalysisFeatures(Collections.singletonList(
                        DocumentAnalysisFeature.KEY_VALUE_PAIRS
                ));
        SyncPoller<AnalyzeOperationDetails, AnalyzeResult> poller =
                docClient.beginAnalyzeDocument("prebuilt-layout", options);
        AnalyzeResult result = poller.getFinalResult();

        // --- Mapping des pages et mots ---
        List<DocumentPage> pages = Optional.ofNullable(result.getPages())
                .orElse(Collections.emptyList())
                .stream()
                .map(p -> {
                    DocumentPage dp = new DocumentPage();
                    dp.setPageNumber(p.getPageNumber());
                    dp.setAngle(p.getAngle());
                    dp.setWidth(p.getWidth());
                    dp.setHeight(p.getHeight());
                    dp.setUnit(p.getUnit().toString());

                    List<DocumentWord> words = Optional.ofNullable(p.getWords())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(w -> {
                                DocumentWord dw = new DocumentWord();
                                dw.setContent(w.getContent());
                                dw.setConfidence(w.getConfidence());
                                dw.setPolygon(Optional.ofNullable(w.getPolygon())
                                        .map(ArrayList::new)
                                        .orElseGet(ArrayList::new));
                                if (w.getSpan() != null) {
                                    Span span = new Span();
                                    span.setOffset(w.getSpan().getOffset());
                                    span.setLength(w.getSpan().getLength());
                                    dw.setSpan(span);
                                }
                                return dw;
                            })
                            .collect(Collectors.toList());

                    dp.setWords(words);
                    return dp;
                })
                .collect(Collectors.toList());

        // --- Mapping des paires clé-valeur ---
        List<KeyValuePair> kvps = Optional.ofNullable(result.getKeyValuePairs())
                .orElse(Collections.emptyList())
                .stream()
                .map(kv -> {
                    KeyValuePair pair = new KeyValuePair();
                    pair.setConfidence(kv.getConfidence());

                    // Key
                    DocumentKey key = new DocumentKey();
                    key.setContent(kv.getKey().getContent());
                    key.setBoundingRegions(Optional.ofNullable(kv.getKey().getBoundingRegions())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(r -> {
                                BoundingRegion br = new BoundingRegion();
                                br.setPageNumber(r.getPageNumber());
                                br.setPolygon(new ArrayList<>(r.getPolygon()));
                                return br;
                            })
                            .collect(Collectors.toList()));
                    key.setSpans(Optional.ofNullable(kv.getKey().getSpans())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(s -> {
                                Span sp = new Span();
                                sp.setOffset(s.getOffset());
                                sp.setLength(s.getLength());
                                return sp;
                            })
                            .collect(Collectors.toList()));
                    pair.setKey(key);

                    // Value (optionnel)
                    if (kv.getValue() != null) {
                        DocumentValue val = new DocumentValue();
                        val.setContent(kv.getValue().getContent());
                        val.setBoundingRegions(Optional.ofNullable(kv.getValue().getBoundingRegions())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(r -> {
                                    BoundingRegion br = new BoundingRegion();
                                    br.setPageNumber(r.getPageNumber());
                                    br.setPolygon(new ArrayList<>(r.getPolygon()));
                                    return br;
                                })
                                .collect(Collectors.toList()));
                        val.setSpans(Optional.ofNullable(kv.getValue().getSpans())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(s -> {
                                    Span sp = new Span();
                                    sp.setOffset(s.getOffset());
                                    sp.setLength(s.getLength());
                                    return sp;
                                })
                                .collect(Collectors.toList()));
                        pair.setValue(val);
                    }
                    return pair;
                })
                .collect(Collectors.toList());

        // --- Mapping des tables et de leurs cellules ---
        List<DocumentTable> tables = Optional.ofNullable(result.getTables())
                .orElse(Collections.emptyList())
                .stream()
                .map(t -> {
                    DocumentTable dt = new DocumentTable();
                    dt.setRowCount(t.getRowCount());
                    dt.setColumnCount(t.getColumnCount());

                    List<DocumentTableCell> cells = Optional.ofNullable(t.getCells())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(c -> {
                                DocumentTableCell cell = new DocumentTableCell();
                                cell.setKind(c.getKind() != null ? c.getKind().toString() : "");
                                cell.setRowIndex(c.getRowIndex());
                                cell.setColumnIndex(c.getColumnIndex());
                                cell.setContent(c.getContent());

                                cell.setBoundingRegions(Optional.ofNullable(c.getBoundingRegions())
                                        .orElse(Collections.emptyList())
                                        .stream()
                                        .map(r -> {
                                            BoundingRegion br = new BoundingRegion();
                                            br.setPageNumber(r.getPageNumber());
                                            br.setPolygon(new ArrayList<>(r.getPolygon()));
                                            return br;
                                        })
                                        .collect(Collectors.toList()));

                                cell.setSpans(Optional.ofNullable(c.getSpans())
                                        .orElse(Collections.emptyList())
                                        .stream()
                                        .map(s -> {
                                            Span sp = new Span();
                                            sp.setOffset(s.getOffset());
                                            sp.setLength(s.getLength());
                                            return sp;
                                        })
                                        .collect(Collectors.toList()));

                                cell.setElements(Optional.ofNullable(c.getElements())
                                        .orElse(Collections.emptyList()));
                                return cell;
                            })
                            .collect(Collectors.toList());

                    dt.setCells(cells);
                    return dt;
                })
                .collect(Collectors.toList());

        // --- Construction et sauvegarde en base ---
        FileDocument doc = new FileDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setFileName(file.getOriginalFilename());
        doc.setAzureUrl(url);
        doc.setUploadedAt(new Date());
        doc.setStatus("IN_REVIEW");
        doc.setPages(pages);
        doc.setKeyValuePairs(kvps);
        doc.setTables(tables);

        return docRepo.save(doc);
    }

    /**
     * Met à jour la liste des paires clé-valeur pour un document existant.
     */
    public FileDocument updateKeyValuePairs(String id, List<KeyValuePair> keyValuePairs) {
        FileDocument doc = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("File not found"));
        doc.setKeyValuePairs(keyValuePairs);
        return docRepo.save(doc);
    }

    /**
     * Met à jour la liste des tables pour un document existant.
     */
    public FileDocument updateTables(String id, List<DocumentTable> tables) {
        FileDocument doc = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("File not found"));
        doc.setTables(tables);
        return docRepo.save(doc);
    }

    /**
     * Approuve toutes les annotations d’un document (change le statut en APPROVED).
     */
    public FileDocument approuveAnnotation(String id) {
        FileDocument doc = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("File not found"));
        doc.setStatus("APPROVED");
        return docRepo.save(doc);
    }
}
