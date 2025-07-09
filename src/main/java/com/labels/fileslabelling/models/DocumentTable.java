package com.labels.fileslabelling.models;

import java.util.List;

/**
 * Représente un tableau extrait d’un document, avec son nombre de lignes, 
 * son nombre de colonnes et la liste détaillée de ses cellules.
 *
 * Chaque instance de DocumentTable fournit :
 *
 *   Le nombre total de lignes (rowCount).
 *   Le nombre total de colonnes (columnCount).
 *   La liste des cellules (cells), chacune décrite par son contenu,
 *       sa position (ligne, colonne) et son étendue (rowSpan, colSpan).
 * 
 * 
 */
public class DocumentTable {

    /**
     * Nombre de lignes détectées dans le tableau.
     * Utile pour positionner les cellules et reconstruire la structure.
     */
    private int rowCount;

    /**
     * Nombre de colonnes détectées dans le tableau.
     * Permet de connaître la largeur logique du tableau.
     */
    private int columnCount;

    /**
     * Liste des cellules composant le tableau.
     * Chaque cellule porte ses coordonnées, son contenu et ses éventuels
     * spans (fusion de lignes ou de colonnes).
     */
    private List<DocumentTableCell> cells;

    /**
     * Retourne le nombre de lignes du tableau.
     *
     * @return rowCount, le nombre total de lignes
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Définit le nombre de lignes du tableau.
     *
     * @param rowCount le nombre de lignes détectées
     */
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    /**
     * Retourne le nombre de colonnes du tableau.
     *
     * @return columnCount, le nombre total de colonnes
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Définit le nombre de colonnes du tableau.
     *
     * @param columnCount le nombre de colonnes détectées
     */
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * Retourne la liste des cellules du tableau.
     *
     * @return cells, une liste de DocumentTableCell
     */
    public List<DocumentTableCell> getCells() {
        return cells;
    }

    /**
     * Affecte la liste des cellules du tableau.
     *
     * @param cells liste de DocumentTableCell à associer à ce tableau
     */
    public void setCells(List<DocumentTableCell> cells) {
        this.cells = cells;
    }
}
