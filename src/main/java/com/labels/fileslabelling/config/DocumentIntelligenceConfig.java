package com.labels.fileslabelling.config;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Spring pour le client Azure Document Intelligence.
 *
 * 
 * Cette classe centralise la création d'un client singleton permettant
 * d'appeler les API d'analyse de documents d'Azure. Les paramètres de
 * connexion (endpoint et clé API) sont injectés depuis les propriétés
 * de l'application pour faciliter la maintenance et la sécurité.
 * 
 */
@Configuration
public class DocumentIntelligenceConfig {

    /**
     * URL du point de terminaison (endpoint) du service Document Intelligence.
     * Exemple : https://<votre-instance>.cognitiveservices.azure.com/
     * Stockée dans le fichier de configuration pour pouvoir la changer sans
     * modifier le code source.
     */
    @Value("${azure.ai.documentintelligence.endpoint}")
    private String endpoint;

    /**
     * Clé d'API utilisée pour authentifier les requêtes vers
     * le service Azure Document Intelligence. À protéger comme un secret.
     */
    @Value("${azure.ai.documentintelligence.apikey}")
    private String apiKey;

    /**
     * Crée et configure le {@link DocumentIntelligenceClient}.
     *
     * 
     *   Configure l'URL de l'endpoint.</li>
     *   Crée un AzureKeyCredential avec la clé API.</li>
     *   Construit et retourne un client prêt à analyser des documents.</li>
     * 
     *
     * @return un client singleton pour l'analyse de documents
     */
    @Bean
    public DocumentIntelligenceClient documentIntelligenceClient() {
        return new DocumentIntelligenceClientBuilder()
            .endpoint(endpoint)
            .credential(new AzureKeyCredential(apiKey))
            .buildClient();
    }
}