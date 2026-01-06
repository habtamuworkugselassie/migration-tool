package com.client.migration.service;

import com.client.migration.dto.ClientMigrationRequest;
import com.client.migration.exception.ClientNotFoundException;
import com.client.migration.exception.MigrationException;
import com.client.migration.model.Client;
import com.client.migration.repository.ClientRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing client migration operations.
 *
 * @author habtamugebreselassie
 */
@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final ExternalProductService externalProductService;

    public ClientService(ClientRepository clientRepository, ExternalProductService externalProductService) {
        this.clientRepository = clientRepository;
        this.externalProductService = externalProductService;
    }

    @PostConstruct
    void init() {
        if (clientRepository.count() == 0) {
            logger.info("Initializing database with sample clients");
            clientRepository.save(new Client(null, "Client A", false));
            clientRepository.save(new Client(null, "Client B", false));
        }
    }

    /**
     * Retrieves all clients that have not been migrated yet.
     *
     * @return List of legacy clients
     */
    public List<Client> getLegacyClients() {
        return clientRepository.findByMigratedFalse();
    }

    /**
     * Retrieves all clients that have been successfully migrated.
     *
     * @return List of migrated clients
     */
    public List<Client> getNewClients() {
        return clientRepository.findByMigratedTrue();
    }

    /**
     * Migrates a client from the legacy system to the new product.
     * This operation is transactional - if the external API call fails, the migration is rolled back.
     *
     * @param id The ID of the client to migrate
     * @throws ClientNotFoundException if the client is not found
     * @throws IllegalStateException if the client is already migrated
     * @throws MigrationException if the migration fails
     */
    @Transactional
    public void migrateClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));

        if (client.isMigrated()) {
            throw new IllegalStateException("Client with id " + id + " is already migrated");
        }

        try {
            ClientMigrationRequest migrationRequest = new ClientMigrationRequest(client.getId(), client.getName());
            externalProductService.migrateClientToNewProduct(migrationRequest);

            client.setMigrated(true);
            clientRepository.save(client);

            logger.info("Successfully migrated client {} ({}) to new product", id, client.getName());
        } catch (Exception e) {
            logger.error("Failed to migrate client {} to new product: {}", id, e.getMessage(), e);
            throw new MigrationException("Migration failed for client " + id + ": " + e.getMessage(), e);
        }
    }
}
