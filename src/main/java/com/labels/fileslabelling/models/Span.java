package com.labels.fileslabelling.models;

import java.util.Objects;

/**
 * Décrit une portion continue de texte par son indice de départ et sa longueur.
 * 
 * 
 * Un Span permet de relier une zone graphique (par exemple un mot ou une valeur)
 * au flux de texte brut complet issu de l’OCR ou de l’analyse documentaire.
 * Le champ <code>offset</code> indique la position du premier caractère dans
 * la séquence globale, et <code>length</code> précise le nombre de caractères
 * concernés.
 * 
 */
public class Span {

    /**
     * Indice (0-based) du premier caractère de la portion
     * dans le texte intégral du document.
     */
    private int offset;

    /**
     * Nombre de caractères inclus dans cette portion de texte.
     */
    private int length;

    /**
     * Retourne l’indice de départ de ce span dans le texte complet.
     *
     * @return offset (0-based)
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Définit l’indice de départ de ce span dans le texte complet.
     *
     * @param offset position (0-based) du premier caractère
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Retourne la longueur de la portion de texte représentée par ce span.
     *
     * @return nombre de caractères
     */
    public int getLength() {
        return length;
    }

    /**
     * Définit la longueur de la portion de texte représentée par ce span.
     *
     * @param length nombre de caractères
     */
    public void setLength(int length) {
        this.length = length;
    }


}
