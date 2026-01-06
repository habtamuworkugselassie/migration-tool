package com.client.migration.service;

import com.client.migration.model.Client;
import com.client.migration.repository.ClientRepository;
import jakarta.annotation.PostConstruct;
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

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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

        client.setMigrated(true);
        clientRepository.save(client);

        System.out.println("Migrated client " + id + " successfully");
    }
}
