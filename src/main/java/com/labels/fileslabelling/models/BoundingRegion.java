package com.labels.fileslabelling.models;

import java.util.List;

public class BoundingRegion {
    private int pageNumber;
    private List<Double> polygon;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<Double> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Double> polygon) {
        this.polygon = polygon;
    }
  

    // getters & setters ...
}