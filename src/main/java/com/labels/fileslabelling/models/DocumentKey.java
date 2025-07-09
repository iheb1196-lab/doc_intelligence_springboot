package com.labels.fileslabelling.models;

import java.util.List;

/**
 * Modèle représentant une clé (ou valeur) extraite d'un document.
 *
 * Un DocumentKey contient le texte extrait, les zones annotées
 * (BoundingRegion) où ce texte apparaît, et les positions de spans
 * pour un traitement plus fin (par exemple, pour le surlignage).
 */
public class DocumentKey {

    /**
     * Contenu textuel extrait (ou reconnu) dans la section du document.
     */
    private String content;

    /**
     * Liste des régions où ce contenu est localisé.
     * Chaque region est définie par un polygone et un numéro de page.
     */
    private List<BoundingRegion> boundingRegions;

    /**
     * Liste des spans correspondant à ce texte (indices de début et fin).
     * Utile pour relier les coordonnées graphiques aux positions dans le texte brut.
     */
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
