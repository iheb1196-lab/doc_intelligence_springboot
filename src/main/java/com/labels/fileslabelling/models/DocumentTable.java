package com.labels.fileslabelling.models;

import java.util.List;

public class DocumentTable {
    private int rowCount;
    private int columnCount;
    private List<DocumentTableCell> cells;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public List<DocumentTableCell> getCells() {
        return cells;
    }

    public void setCells(List<DocumentTableCell> cells) {
        this.cells = cells;
    }


    // getters & setters ...
}