package com.labels.fileslabelling.config;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentIntelligenceConfig {

    @Value("${azure.ai.documentintelligence.endpoint}")
    private String endpoint;

    @Value("${azure.ai.documentintelligence.apikey}")
    private String apiKey;

    /**
     * Creates a singleton DocumentIntelligenceClient for calling the service.
     */
    @Bean
    public DocumentIntelligenceClient documentIntelligenceClient() {
        return new DocumentIntelligenceClientBuilder()
            .endpoint(endpoint)
            .credential(new AzureKeyCredential(apiKey))
            .buildClient();
    }
}