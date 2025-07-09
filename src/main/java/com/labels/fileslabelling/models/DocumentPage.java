package com.labels.fileslabelling.models;

import java.util.List;

/**
 * Représente une page d’un document analysé, avec ses métadonnées et
 * la liste des mots reconnus sur cette page.
 *
 * Chaque DocumentPage fournit :
 * 
 *   Le numéro de page (1-indexé).
 *   L’angle de rotation détecté, pour corriger l’orientation si besoin.
 *   Les dimensions physiques de la page (largeur et hauteur) et l’unité de mesure.
 *   La liste ordonnée des mots extraits, avec leur position et style.
 * 
 * 
 */
public class DocumentPage {

    /**
     * Numéro de la page dans le document.
     * Utile pour associer chaque page à son contenu et pour la pagination.
     */
    private int pageNumber;

    /**
     * Angle de rotation détecté sur la page (en degrés).
     * Permet de réaligner le contenu lorsqu’un document n’est pas droit.
     */
    private double angle;

    /**
     * Largeur de la page, selon l’unité spécifiée.
     * Exemple : 8.5 pour 8,5 pouces ou 612 pour 612 points.
     */
    private double width;

    /**
     * Hauteur de la page, selon l’unité spécifiée.
     * Exemple : 11 pour 11 pouces ou 792 pour 792 points.
     */
    private double height;

    /**
     * Unité de mesure utilisée pour width et height.
     * Par exemple \"inch\", \"point\" ou \"pixel\".
     */
    private String unit;

    /**
     * Liste des mots détectés sur cette page.
     * Chaque mot inclut son contenu textuel, sa position et ses attributs de style.
     */
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
}
