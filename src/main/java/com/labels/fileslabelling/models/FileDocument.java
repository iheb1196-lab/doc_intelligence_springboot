package com.labels.fileslabelling.models;

import com.azure.spring.data.cosmos.core.mapping.Container;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Container(containerName = "files")
public class FileDocument {
    @Id
    private String id;
    private String fileName;
    private String azureUrl;
    private Date uploadedAt;
    private String status; // IN_REVIEW or APPROVED
    

    // NEW: Raw pages from DocumentIntelligence
    private List<DocumentPage> pages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAzureUrl() {
        return azureUrl;
    }

    public void setAzureUrl(String azureUrl) {
        this.azureUrl = azureUrl;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DocumentPage> getPages() {
        return pages;
    }

    public void setPages(List<DocumentPage> pages) {
        this.pages = pages;
    }

    public List<KeyValuePair> getKeyValuePairs() {
        return keyValuePairs;
    }

    public void setKeyValuePairs(List<KeyValuePair> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }

    public List<DocumentTable> getTables() {
        return tables;
    }

    public void setTables(List<DocumentTable> tables) {
        this.tables = tables;
    }

    // NEW: Key/Value pairs
    private List<KeyValuePair> keyValuePairs;

    // NEW: Tables with every cell
    private List<DocumentTable> tables;

    // getters & setters omitted for brevity...
    // (just generate for all seven fields above)
}
