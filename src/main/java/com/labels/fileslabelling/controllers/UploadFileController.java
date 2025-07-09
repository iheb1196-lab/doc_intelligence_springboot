package com.labels.fileslabelling.controllers;

import com.labels.fileslabelling.models.FileDocument;
import com.labels.fileslabelling.models.KeyValuePair;
import com.labels.fileslabelling.models.DocumentTable;
import com.labels.fileslabelling.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contrôleur REST exposant les endpoints pour gérer les fichiers labellisés :
 *   • Liste des fichiers
 *   • Téléversement et analyse d’un nouveau fichier
 *   • Récupération des détails d’un fichier
 *   • Mise à jour des paires clé–valeur
 *   • Mise à jour des tables
 *   • Approbation des annotations
 *
 * Toutes les opérations déléguent la logique métier au {@link FileService}.
 */
@RestController
@RequestMapping("/api/files")
public class UploadFileController {

    private final FileService fileService;

    /**
     * Injection du service de traitement des fichiers.
     *
     * @param fileService service contenant la logique d’upload, d’analyse et de persistence
     */
    public UploadFileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Retourne la liste de tous les fichiers stockés.
     * Chaque entrée contient l’ID, le nom, le statut et la date de téléversement.
     *
     * GET /api/files
     *
     * @return 200 OK + liste simplifiée des documents
     */
    @GetMapping
    public ResponseEntity<List<Map<String,Object>>> listFiles() {
        List<Map<String,Object>> out = fileService.findAll().stream()
            .map(d -> Map.<String,Object>of(
                "id",         d.getId(),
                "fileName",   d.getFileName(),
                "status",     d.getStatus(),
                "uploadedAt", d.getUploadedAt()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    /**
     * Téléverse un nouveau fichier et déclenche son analyse.
     * En cas de succès, retourne l’objet FileDocument complet, incluant
     * pages, paires clé–valeur et tables extraites.
     *
     * POST /api/files/upload
     *
     * @param file fichier multipart envoyé par le client
     * @return 400 Bad Request si le fichier est vide, sinon 200 OK + FileDocument
     * @throws Exception en cas d’erreur d’upload ou d’analyse
     */
    @PostMapping("/upload")
    public ResponseEntity<FileDocument> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        FileDocument saved = fileService.uploadAndAnalyze(file);
        return ResponseEntity.ok(saved);
    }

    /**
     * Récupère les détails d’un fichier déjà analysé.
     *
     * GET /api/files/{fileId}
     *
     * @param fileId identifiant unique du document
     * @return 200 OK + FileDocument si trouvé, sinon 404 Not Found
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<FileDocument> getFile(@PathVariable String fileId) {
        return fileService.findById(fileId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Met à jour manuellement les paires clé–valeur d’un document.
     * Utile pour corriger ou enrichir les résultats d’analyse.
     *
     * PATCH /api/files/{fileId}/keyValuePairs
     *
     * @param fileId identifiant du document à modifier
     * @param edits liste des nouvelles paires clé–valeur
     * @return 200 OK + FileDocument mis à jour
     */
    @PatchMapping("/{fileId}/keyValuePairs")
    public ResponseEntity<FileDocument> patchKeyValuePairs(
        @PathVariable String fileId,
        @RequestBody List<KeyValuePair> edits
    ) {
        return ResponseEntity.ok(
          fileService.updateKeyValuePairs(fileId, edits)
        );
    }

    /**
     * Met à jour manuellement les tables extraites d’un document.
     * Permet de corriger la structure du tableau ou le contenu des cellules.
     *
     * PATCH /api/files/{fileId}/tables
     *
     * @param fileId identifiant du document à modifier
     * @param edits liste des nouvelles tables
     * @return 200 OK + FileDocument mis à jour
     */
    @PatchMapping("/{fileId}/tables")
    public ResponseEntity<FileDocument> patchTables(
        @PathVariable String fileId,
        @RequestBody List<DocumentTable> edits
    ) {
        return ResponseEntity.ok(
          fileService.updateTables(fileId, edits)
        );
    }

    /**
     * Change le statut d’un document en « APPROVED », marquant les annotations
     * comme validées et prêtes à être utilisées.
     *
     * PATCH /api/files/{fileId}/approve
     *
     * @param fileId identifiant du document à approuver
     * @return 200 OK + FileDocument mis à jour
     */
    @PatchMapping("/{fileId}/approve")
    public ResponseEntity<FileDocument> approuveAnnotation(
        @PathVariable String fileId
    ) {
        return ResponseEntity.ok(
          fileService.approuveAnnotation(fileId)
        );
    }
}
