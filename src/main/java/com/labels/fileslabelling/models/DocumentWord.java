package com.labels.fileslabelling.models;

import java.util.List;

/**
 * Représente un mot détecté sur une page de document avec ses informations
 * de contenu, position et fiabilité.
 *
 * Chaque DocumentWord fournit :
 * 
 *   Le texte reconnu du mot.
 *   Le polygone (liste de coordonnées) délimitant la zone du mot à l’écran.
 *   Le score de confiance (confidence) renvoyé par le service d’OCR.
 *   Le span textuel, c’est-à-dire les indices start/end dans le flux de texte brut.
 * 
 */ 
public class DocumentWord {

    /**
     * Texte brut reconnu pour ce mot.
     */
    private String content;

    /**
     * Coordonnées successives des sommets du polygone entourant le mot.
     * Format : [x1, y1, x2, y2, ..., xn, yn]
     */
    private List<Double> polygon;

    /**
     * Score de confiance (entre 0 et 1) indiquant la fiabilité de la reconnaissance.
     */
    private double confidence;

    /**
     * Position du mot dans le flux de texte complet (startIndex / endIndex).
     * Permet de faire le lien avec le contenu textuel global du document.
     */
    private Span span;

    /** Retourne le texte reconnu. */
    public String getContent() {
        return content;
    }

    /** Définit le texte reconnu pour ce mot. */
    public void setContent(String content) {
        this.content = content;
    }

    /** Retourne les coordonnées du polygone entourant le mot. */
    public List<Double> getPolygon() {
        return polygon;
    }

    /** Définit les coordonnées du polygone entourant le mot. */
    public void setPolygon(List<Double> polygon) {
        this.polygon = polygon;
    }

    /** Retourne le score de confiance de la reconnaissance. */
    public double getConfidence() {
        return confidence;
    }

    /** Définit le score de confiance de la reconnaissance. */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /** Retourne le span textuel (indices start/end). */
    public Span getSpan() {
        return span;
    }

    /** Définit le span textuel (indices start/end). */
    public void setSpan(Span span) {
        this.span = span;
    }
}
