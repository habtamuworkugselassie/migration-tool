package com.client.migration.service;

import com.client.migration.config.MigrationProperties;
import com.client.migration.dto.ClientMigrationRequest;
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
 * @author habtamugebreselassie
 * Date: 06/01/2026
 * Time: 11:20
 * Service for communicating with external new product API
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

    public void migrateClientToNewProduct(ClientMigrationRequest request) {
        if (!migrationProperties.isEnableExternalMigration()) {

            logger.info("External migration is disabled. Skipping API call for client: {}", request.getId());
            return;
        }

        if (migrationProperties.getNewProductApiUrl() == null || migrationProperties.getNewProductApiUrl().isEmpty()) {

            logger.warn("New product API URL is not configured. Skipping external migration for client: {}", request.getId());
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ClientMigrationRequest> entity = new HttpEntity<>(request, headers);

            logger.info("Calling new product API to migrate client: {} to URL: {}", request.getId(), migrationProperties.getNewProductApiUrl());

            ResponseEntity<String> response = restTemplate.exchange(
                    migrationProperties.getNewProductApiUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully migrated client {} to new product. Response: {}", request.getId(), response.getBody());
            } else {
                throw new RuntimeException("Failed to migrate client to new product. Status: " + response.getStatusCode());
            }
        } catch (RestClientException e) {

            logger.error("Error calling new product API for client {}: {}", request.getId(), e.getMessage());

            throw new RuntimeException("Failed to migrate client to new product: " + e.getMessage(), e);
        }
    }
}

