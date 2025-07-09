package com.labels.fileslabelling.models;

import java.util.List;

/**
 * Représente une cellule individuelle d’un tableau extrait d’un document.
 * 
 * Chaque DocumentTableCell contient :
 * 
 *   Le type de cellule (en-tête, corps, pied de page, etc.).
 *   Sa position dans la grille (index de ligne et de colonne, 0-indexés).
 *   Le contenu textuel reconnu dans la cellule.
 *   La ou les régions graphiques (polygones) où ce contenu apparaît.
 *   Les spans, c’est-à-dire les intervalles de texte dans le flux brut.
 *   Éventuellement une liste d’éléments annexes (images, puces, etc.).
 * 
 */
public class DocumentTableCell {
    
    /**
     * Type de cellule : e.g. "columnHeader", "rowHeader", "body", "footer".
     */
    private String kind;

    /**
     * Index (0-based) de la ligne où se trouve cette cellule.
     */
    private int rowIndex;

    /**
     * Index (0-based) de la colonne où se trouve cette cellule.
     */
    private int columnIndex;

    /**
     * Texte reconnu dans cette cellule.
     */
    private String content;

    /**
     * Zones graphiques (polygones) délimitant visuellement le contenu.
     * Chaque polygone est représenté comme une liste [x1, y1, ..., xn, yn].
     */
    private List<BoundingRegion> boundingRegions;

    /**
     * Positions textuelles dans le flux brut, permettant de relier
     * la cellule au texte source (ex. indices start/end).
     */
    private List<Span> spans;

    /**
     * Contenu complexe ou éléments supplémentaires, par exemple
     * des listes à puces, images ou formules détectées dans la cellule.
     */
    private List<String> elements;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

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

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }
}
