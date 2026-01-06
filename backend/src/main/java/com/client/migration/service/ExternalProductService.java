package com.client.migration.service;

import com.client.migration.config.MigrationProperties;
import com.client.migration.dto.ClientMigrationRequest;
import com.client.migration.exception.MigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service for communicating with the external new product API.
 * Handles HTTP requests to forward client data during migration.
 *
 * @author habtamugebreselassie
 */
@Service
public class ExternalProductService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalProductService.class);
    private final RestTemplate restTemplate;
    private final MigrationProperties migrationProperties;

    public ExternalProductService(RestTemplate restTemplate,
                                  MigrationProperties migrationProperties) {
        this.restTemplate = restTemplate;
        this.migrationProperties = migrationProperties;
    }

    /**
     * Migrates a client to the new product by calling the external API.
     *
     * @param request The client migration request containing client data
     * @throws MigrationException if the migration fails
     */
    public void migrateClientToNewProduct(ClientMigrationRequest request) {

        if (!migrationProperties.isEnableExternalMigration()) {
            logger.info("External migration is disabled. Skipping API call for client: {}", request.getId());
            return;
        }

        String apiUrl = migrationProperties.getNewProductApiUrl();

        if (apiUrl == null || apiUrl.isEmpty()) {
            logger.warn("New product API URL is not configured. Skipping external migration for client: {}", request.getId());
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ClientMigrationRequest> entity = new HttpEntity<>(request, headers);

            logger.info("Calling new product API to migrate client: {} to URL: {}", request.getId(), apiUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully migrated client {} to new product. Response: {}", request.getId(), response.getBody());
            } else {
                throw new MigrationException("Failed to migrate client to new product. Status: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("Error calling new product API for client {}: {}", request.getId(), e.getMessage(), e);
            throw new MigrationException("Failed to migrate client to new product: " + e.getMessage(), e);
        }
    }
}

