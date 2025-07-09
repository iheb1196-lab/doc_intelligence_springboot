// src/main/java/com/labels/fileslabelling/controller/UploadFileController.java
package com.labels.fileslabelling;

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

@RestController
@RequestMapping("/api/files")

public class UploadFileController {

    private final FileService fileService;

    public UploadFileController(FileService fileService) {
        this.fileService = fileService;
    }
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
    @PostMapping("/upload")
    public ResponseEntity<FileDocument> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        FileDocument saved = fileService.uploadAndAnalyze(file);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileDocument> getFile(@PathVariable String fileId) {
        return fileService.findById(fileId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

  @PatchMapping("/{fileId}/keyValuePairs")
public ResponseEntity<FileDocument> patchKeyValuePairs(
    @PathVariable String fileId,
    @RequestBody List<KeyValuePair> edits
) {
    return ResponseEntity.ok(
      fileService.updateKeyValuePairs(fileId, edits)
    );
}

  @PatchMapping("/{fileId}/tables")
public ResponseEntity<FileDocument> patchTables(
    @PathVariable String fileId,
    @RequestBody List<DocumentTable> edits
) {
    return ResponseEntity.ok(
      fileService.updateTables(fileId, edits)
    );
}
  @PatchMapping("/{fileId}/approve")
public ResponseEntity<FileDocument> approuveAnnotation(
    @PathVariable String fileId
 
) {
    return ResponseEntity.ok(
      fileService.approuveAnnotation(fileId)
    );
}
}
