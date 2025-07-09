package com.labels.fileslabelling.models;

import java.util.List;

/**
 * Représente une région annotée dans un document PDF ou image.
 * 
 * Chaque instance décrit la page concernée et la forme polygonale
 * de la zone labellée. Le polygone est une liste de coordonnées
 * [x1, y1, x2, y2, ..., xn, yn], permettant de dessiner la forme
 * exacte de l'annotation.
 * 
 */
public class BoundingRegion {

    /**
     * Numéro de la page sur laquelle se trouve la région annotée.
     * Utile pour repérer l'emplacement de l'annotation dans le document.
     */
    private int pageNumber;

    /**
     * Liste des points formant le polygone de la région annotée.
     * Chaque paire de valeurs représente les coordonnées x et y d'un sommet.
     */
    private List<Double> polygon;

    /**
     * Retourne le numéro de page associé à cette région.
     *
     * @return le numéro de page (1-indexé)
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Définit le numéro de page sur lequel la région est annotée.
     *
     * @param pageNumber le numéro de page (1-indexé)
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Retourne la liste des coordonnées du polygone définissant la région.
     *
     * @return une liste de doubles [x1, y1, ..., xn, yn]
     */
    public List<Double> getPolygon() {
        return polygon;
    }

    /**
     * Affecte les coordonnées du polygone pour cette région.
     *
     * @param polygon liste de sommets [x1, y1, ..., xn, yn]
     */
    public void setPolygon(List<Double> polygon) {
        this.polygon = polygon;
    }
}