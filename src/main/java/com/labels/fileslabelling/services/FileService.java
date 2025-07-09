// src/main/java/com/labels/fileslabelling/services/FileService.java
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
private void waitForBlobAvailability(BlobClient blob) throws InterruptedException {
    int maxRetries = 10;
    int retries = 0;

    // Poll every half-second until Azure says the blob exists, or we give up
    while (retries < maxRetries && !blob.exists()) {
        Thread.sleep(500);
        retries++;
    }
    // If you need, you can throw after maxRetries to fail fast
}
    public List<FileDocument> findAll() {
        return docRepo.findAll();
    }

    public Optional<FileDocument> findById(String id) {
        return docRepo.findById(id);
    }

    public FileDocument uploadAndAnalyze(MultipartFile file)  throws IOException, InterruptedException {
        // 1) Upload to Blob
        String blobName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobClient blob = blobContainerClient.getBlobClient(blobName);
        blob.upload(file.getInputStream(), file.getSize(), true);
        waitForBlobAvailability(blob);
        System.out.println(blob.exists());
        String url = blob.getBlobUrl();

        // 2) Analyze: request LAYOUT + KEY_VALUE_PAIRS + TABLES
        AnalyzeDocumentOptions options = new AnalyzeDocumentOptions(url)
            .setDocumentAnalysisFeatures(Arrays.asList(
                DocumentAnalysisFeature.KEY_VALUE_PAIRS
                
            ));

        SyncPoller<AnalyzeOperationDetails, AnalyzeResult> poller =
            docClient.beginAnalyzeDocument("prebuilt-layout", options);
        AnalyzeResult result = poller.getFinalResult();

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
                        dw.setPolygon(
                          Optional.ofNullable(w.getPolygon())
                                  .map(ArrayList::new)
                                  .orElseGet(ArrayList::new)
                        );

                        // span is guaranteed non‐null by SDK, but just in case:
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

        // 4) Map key/value pairs (null‐safe)
        List<KeyValuePair> kvps = Optional.ofNullable(result.getKeyValuePairs())
            .orElse(Collections.emptyList())
            .stream()
            .map(kv -> {
                KeyValuePair pair = new KeyValuePair();
                pair.setConfidence(kv.getConfidence());

                // -- key (always present) --
                DocumentKey key = new DocumentKey();
                key.setContent(kv.getKey().getContent());
                key.setBoundingRegions(
                  Optional.ofNullable(kv.getKey().getBoundingRegions())
                          .orElse(Collections.emptyList())
                          .stream()
                          .map(r -> {
                              BoundingRegion br = new BoundingRegion();
                              br.setPageNumber(r.getPageNumber());
                              br.setPolygon(new ArrayList<>(r.getPolygon()));
                              return br;
                          })
                          .collect(Collectors.toList())
                );
                key.setSpans(
                  Optional.ofNullable(kv.getKey().getSpans())
                          .orElse(Collections.emptyList())
                          .stream()
                          .map(s -> {
                              Span sp = new Span();
                              sp.setOffset(s.getOffset());
                              sp.setLength(s.getLength());
                              return sp;
                          })
                          .collect(Collectors.toList())
                );
                pair.setKey(key);

                // -- value (may be null) --
                if (kv.getValue() != null) {
                  DocumentValue val = new DocumentValue();
                  val.setContent(kv.getValue().getContent());
                  val.setBoundingRegions(
                    Optional.ofNullable(kv.getValue().getBoundingRegions())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(r -> {
                                BoundingRegion br = new BoundingRegion();
                                br.setPageNumber(r.getPageNumber());
                                br.setPolygon(new ArrayList<>(r.getPolygon()));
                                return br;
                            })
                            .collect(Collectors.toList())
                  );
                  val.setSpans(
                    Optional.ofNullable(kv.getValue().getSpans())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(s -> {
                                Span sp = new Span();
                                sp.setOffset(s.getOffset());
                                sp.setLength(s.getLength());
                                return sp;
                            })
                            .collect(Collectors.toList())
                  );
                  pair.setValue(val);
                }

                return pair;
            })
            .collect(Collectors.toList());

        // 5) Map tables + cells (null‐safe)
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
                        cell.setKind(
                   c.getKind() != null
                     ? c.getKind().toString()
                    : ""                 );
                        cell.setRowIndex(c.getRowIndex());
                        cell.setColumnIndex(c.getColumnIndex());
                        cell.setContent(c.getContent());

                        cell.setBoundingRegions(
                          Optional.ofNullable(c.getBoundingRegions())
                                  .orElse(Collections.emptyList())
                                  .stream()
                                  .map(r -> {
                                      BoundingRegion br = new BoundingRegion();
                                      br.setPageNumber(r.getPageNumber());
                                      br.setPolygon(new ArrayList<>(r.getPolygon()));
                                      return br;
                                  })
                                  .collect(Collectors.toList())
                        );

                        cell.setSpans(
                          Optional.ofNullable(c.getSpans())
                                  .orElse(Collections.emptyList())
                                  .stream()
                                  .map(s -> {
                                      Span sp = new Span();
                                      sp.setOffset(s.getOffset());
                                      sp.setLength(s.getLength());
                                      return sp;
                                  })
                                  .collect(Collectors.toList())
                        );

                        cell.setElements(
                          Optional.ofNullable(c.getElements())
                                  .orElse(Collections.emptyList())
                        );
                        return cell;
                    })
                    .collect(Collectors.toList());

                dt.setCells(cells);
                return dt;
            })
            .collect(Collectors.toList());

        // 6) Build & save
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

public FileDocument updateKeyValuePairs(
    String id,
    List<KeyValuePair> keyValuePairs
) {
    FileDocument doc = findById(id)
        .orElseThrow(() -> new IllegalArgumentException("File not found"));
    doc.setKeyValuePairs(keyValuePairs);
    return docRepo.save(doc);
}


public FileDocument updateTables(
    String id,
    List<DocumentTable> tables
) {
    FileDocument doc = findById(id)
        .orElseThrow(() -> new IllegalArgumentException("File not found"));
    doc.setTables(tables);
    return docRepo.save(doc);
}
public FileDocument approuveAnnotation(
    String id
) {
    FileDocument doc = findById(id)
        .orElseThrow(() -> new IllegalArgumentException("File not found"));
    doc.setStatus("APPROVED");
    return docRepo.save(doc);
}
    

    
}
