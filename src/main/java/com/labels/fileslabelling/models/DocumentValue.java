package com.labels.fileslabelling.models;

import java.util.List;

/**
 * Représente une valeur extraite d’un document (par exemple dans un formulaire ou un tableau),
 * avec son contenu textuel et les informations de position associées.
 *
 * Chaque DocumentValue fournit :
 * 
 *   Le texte brut reconnu.
 *   Les régions graphiques (polygones) où ce texte apparaît.
 *   Les spans, c’est-à-dire les indices de début et de fin dans le flux de texte complet.
 * 
 */
public class DocumentValue {

    /**
     * Texte reconnu correspondant à cette valeur.
     * Généralement un champ de formulaire, un montant, une date, etc.
     */
    private String content;

    /**
     * Liste des zones (polygones) délimitant visuellement ce contenu dans le document.
     * Chaque polygone est décrit par une séquence de coordonnées [x1, y1, ..., xn, yn].
     */
    private List<BoundingRegion> boundingRegions;

    /**
     * Liste des spans reliant cette valeur au texte brut complet,
     * permettant la corrélation entre la position physique et l’indexation textuelle.
     */
    private List<Span> spans;

    /**
     * Retourne le texte reconnu de cette valeur.
     *
     * @return le contenu textuel extrait
     */
    public String getContent() {
        return content;
    }

    /**
     * Définit le texte reconnu pour cette valeur.
     *
     * @param content le contenu textuel à stocker
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retourne les régions graphiques associées à cette valeur.
     *
     * @return liste de BoundingRegion décrivant la ou les zones
     */
    public List<BoundingRegion> getBoundingRegions() {
        return boundingRegions;
    }

    /**
     * Définit les régions graphiques où apparaît ce contenu.
     *
     * @param boundingRegions liste de polygones [x1, y1, ..., xn, yn]
     */
    public void setBoundingRegions(List<BoundingRegion> boundingRegions) {
        this.boundingRegions = boundingRegions;
    }

    /**
     * Retourne les spans textuels pour cette valeur.
     *
     * @return liste de Span indiquant les positions dans le texte brut
     */
    public List<Span> getSpans() {
        return spans;
    }

    /**
     * Définit les spans textuels correspondant à cette valeur.
     *
     * @param spans liste de Span (startIndex, endIndex)
     */
    public void setSpans(List<Span> spans) {
        this.spans = spans;
    }
}

