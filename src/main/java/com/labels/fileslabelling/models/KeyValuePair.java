package com.labels.fileslabelling.models;

import java.util.Objects;

/**
 * Représente une paire clé–valeur extraite d’un document,
 * assortie d’un score de confiance global.
 *
 * Chaque KeyValuePair contient :
 * 
 *   Un DocumentKey (la clé, ex. \"Date\", \"Montant\", etc.).
 *   Un DocumentValue (la valeur correspondante).
 *   Un score de confiance (entre 0 et 1) pour valoriser la fiabilité de l’association.
 * 
 */
public class KeyValuePair {

    /**
     * Clé extraite du document, avec son contenu textuel et ses régions d’apparition.
     */
    private DocumentKey key;

    /**
     * Valeur associée à la clé, avec son contenu textuel et ses régions d’apparition.
     */
    private DocumentValue value;

    /**
     * Score de confiance global pour la paire, reflétant la fiabilité
     * de la correspondance entre clé et valeur.
     */
    private double confidence;

    /**
     * Retourne la clé de la paire.
     *
     * @return l’objet DocumentKey représentant la clé
     */
    public DocumentKey getKey() {
        return key;
    }

    /**
     * Définit la clé de la paire.
     *
     * @param key instance de DocumentKey à associer
     */
    public void setKey(DocumentKey key) {
        this.key = key;
    }

    /**
     * Retourne la valeur de la paire.
     *
     * @return l’objet DocumentValue représentant la valeur
     */
    public DocumentValue getValue() {
        return value;
    }

    /**
     * Définit la valeur de la paire.
     *
     * @param value instance de DocumentValue à associer
     */
    public void setValue(DocumentValue value) {
        this.value = value;
    }

    /**
     * Retourne le score de confiance de la paire clé–valeur.
     *
     * @return confiance entre 0.0 et 1.0
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Définit le score de confiance de la paire clé–valeur.
     *
     * @param confidence valeur entre 0.0 et 1.0
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

}

