// src/main/java/com/labels/fileslabelling/models/LineItem.java
package com.labels.fileslabelling.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class LineItem {

    private String id = UUID.randomUUID().toString();
    private Map<String, String> fields = new LinkedHashMap<>();

    // No-arg constructor for Spring Data
    public LineItem() {
    }

    public String getId() {
        return id;
    }

    /** Setter so the persistence layer can write back the stored ID */
    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}