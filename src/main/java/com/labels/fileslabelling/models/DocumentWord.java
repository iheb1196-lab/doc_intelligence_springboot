package com.labels.fileslabelling.models;

import java.util.List;

public class DocumentWord {
    private String content;
    private List<Double> polygon;
    private double confidence;
    private Span span;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Double> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Double> polygon) {
        this.polygon = polygon;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    // getters & setters ...
}