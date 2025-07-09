package com.labels.fileslabelling.models;

public class KeyValuePair {
    private DocumentKey key;
    private DocumentValue value;
    private double confidence;

    public DocumentKey getKey() {
        return key;
    }

    public void setKey(DocumentKey key) {
        this.key = key;
    }

    public DocumentValue getValue() {
        return value;
    }

    public void setValue(DocumentValue value) {
        this.value = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    // getters & setters ...
}

