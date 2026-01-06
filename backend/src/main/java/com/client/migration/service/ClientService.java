package com.client.migration.service;

import com.client.migration.dto.ClientMigrationRequest;
import com.client.migration.model.Client;
import com.client.migration.repository.ClientRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author habtamugebreselassie
 * Date: 06/01/2026
 * Time: 10:44
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
            clientRepository.save(new Client(null, "Client A", false));
            clientRepository.save(new Client(null, "Client B", false));
        }
    }

    public List<Client> getLegacyClients() {
        return clientRepository.findByMigratedFalse();
    }

    public List<Client> getNewClients() {
        return clientRepository.findByMigratedTrue();
    }

    @Transactional
    public void migrateClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (client.isMigrated()) {
            throw new IllegalStateException("Client already migrated");
        }

        try {
            ClientMigrationRequest migrationRequest = new ClientMigrationRequest(client.getId(), client.getName());
            externalProductService.migrateClientToNewProduct(migrationRequest);

            client.setMigrated(true);
            clientRepository.save(client);

            logger.info("Successfully migrated client {} to new product", id);
        } catch (Exception e) {
            logger.error("Failed to migrate client {} to new product: {}", id, e.getMessage());
            throw new RuntimeException("Migration failed: " + e.getMessage(), e);
        }
    }
}
