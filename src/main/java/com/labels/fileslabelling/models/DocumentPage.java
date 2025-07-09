package com.labels.fileslabelling.models;

import java.util.List;

public class DocumentPage {
    private int pageNumber;
    private double angle;
    private double width;
    private double height;
    private String unit;
    private List<DocumentWord> words;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<DocumentWord> getWords() {
        return words;
    }

    public void setWords(List<DocumentWord> words) {
        this.words = words;
    }

    // getters & setters ...
}