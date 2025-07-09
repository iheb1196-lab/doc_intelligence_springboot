package com.labels.fileslabelling.models;

import java.util.List;

public class DocumentValue {
    private String content;
    private List<BoundingRegion> boundingRegions;
    private List<Span> spans;

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public List<BoundingRegion> getBoundingRegions() {
        return boundingRegions;
    }
    public void setBoundingRegions(List<BoundingRegion> boundingRegions) {
        this.boundingRegions = boundingRegions;
    }

    public List<Span> getSpans() {
        return spans;
    }
    public void setSpans(List<Span> spans) {
        this.spans = spans;
    }
}
