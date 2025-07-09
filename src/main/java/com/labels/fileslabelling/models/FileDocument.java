package com.labels.fileslabelling.models;

import com.azure.spring.data.cosmos.core.mapping.Container;
import org.springframework.data.annotation.Id;
import java.util.Date;
import java.util.List;

/**
 * Représente un document stocké, avec ses métadonnées et
 * les résultats bruts de l’analyse (pages, paires clé-valeur, tables).
 *
 * Chaque FileDocument contient :
 * 
 *   Un identifiant unique (id) pour la base Cosmos DB.
 *   Le nom de fichier original et l’URL Azure Blob pour y accéder.
 *   La date de téléversement, pour suivre l’historique des imports.
 *   Le statut du document (« IN_REVIEW » ou « APPROVED »).
 *   La liste des pages analysées (pages), avec chaque mot extrait, son orientation, etc.
 *   Les paires clé-valeur détectées (keyValuePairs), pour extraire les données structurées.
 *   Les tables identifiées (tables), avec leurs cellules détaillées.
 * 
 */
@Container(containerName = "files")
public class FileDocument {

    /**
     * Identifiant unique généré par Cosmos DB.
     */
    @Id
    private String id;

    /**
     * Nom original du fichier uploadé.
     */
    private String fileName;

    /**
     * URL Azure Blob Storage pointant vers le document.
     */
    private String azureUrl;

    /**
     * Horodatage du téléversement, utilisé pour trier et historiser.
     */
    private Date uploadedAt;

    /**
     * Statut actuel du document dans le workflow de labellisation.
     * Valeurs possibles : IN_REVIEW, APPROVED.
     */
    private String status;

    /**
     * Pages brutes renvoyées par Document Intelligence,
     * incluant mots détectés, orientation et dimensions.
     */
    private List<DocumentPage> pages;

    /**
     * Paires clé-valeur extraites, chacune avec son texte,
     * ses coordonnées et ses spans textuels.
     */
    private List<KeyValuePair> keyValuePairs;

    /**
     * Tables reconnues dans le document, avec leurs cellules et
     * informations de fusion (rowSpan, colSpan).
     */
    private List<DocumentTable> tables;

    // ————————————————————— Getter & Setter —————————————————————

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAzureUrl() {
        return azureUrl;
    }

    public void setAzureUrl(String azureUrl) {
        this.azureUrl = azureUrl;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DocumentPage> getPages() {
        return pages;
    }

    public void setPages(List<DocumentPage> pages) {
        this.pages = pages;
    }

    public List<KeyValuePair> getKeyValuePairs() {
        return keyValuePairs;
    }

    public void setKeyValuePairs(List<KeyValuePair> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }

    public List<DocumentTable> getTables() {
        return tables;
    }

    public void setTables(List<DocumentTable> tables) {
        this.tables = tables;
    }
}
