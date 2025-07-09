package com.labels.fileslabelling.config;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Spring pour la connexion et l'utilisation d'Azure Blob Storage.
 *
 * 
 * Cette classe initialise le client nécessaire pour interagir avec le service
 * de stockage Blob d'Azure. Elle lit la chaîne de connexion et le nom du
 * conteneur depuis le fichier de propriétés (application.properties ou
 * application.yml), crée automatiquement le conteneur s'il n'existe pas,
 * et expose un bean {@link BlobContainerClient} pour permettre les opérations
 * d'upload, download et gestion des blobs.
 * 
 */
@Configuration
public class AzureStorageConfig {

    /**
     * Chaîne de connexion complète pour accéder à votre compte Azure Storage.
     * Elle contient toutes les informations nécessaires (clé de compte,
     * points de terminaison, etc.) et doit être stockée de manière sécurisée
     * dans le fichier de configuration de l'application.
     */
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    /**
     * Nom du conteneur Blob où seront stockés tous les fichiers labellisés.
     * Vous pouvez isoler plusieurs environnements (dev, test, prod) en ayant
     * des conteneurs différents pour chacun.
     */
    @Value("${azure.storage.container-name}")
    private String containerName;

    /**
     * Crée et configure le bean {@link BlobContainerClient}.
     *
     * 
     *   Construit un {@link BlobServiceClient} à partir de la chaîne de connexion.</li>
     *   Récupère un {@link BlobContainerClient} pour le conteneur spécifié.</li>
     *   Vérifie si le conteneur existe ; si non, le crée automatiquement.</li>
     *   Retourne le client prêt à l'emploi pour toutes les opérations de blob.</li>
     * 
     *
     * @return un BlobContainerClient prêt à être injecté et utilisé
     */
    @Bean
    public BlobContainerClient blobContainerClient() {
        // Étape 1 : construction du client de service Azure Blob Storage
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
            .connectionString(connectionString)
            .buildClient();

        // Étape 2 : obtention du client pour le conteneur spécifié
        BlobContainerClient container = serviceClient.getBlobContainerClient(containerName);

        // Étape 3 : création du conteneur si celui-ci n'existe pas encore
        if (!container.exists()) {
            container.create();
        }

        // Étape 4 : renvoi du client pour utilisation dans l'application
        return container;
    }
}