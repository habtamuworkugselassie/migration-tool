package com.client.migration.controller;

import com.client.migration.model.Client;
import com.client.migration.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for client migration operations.
 *
 * @author habtamugebreselassie
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    /**
     * Retrieves all legacy clients that need to be migrated.
     *
     * @return List of legacy clients
     */
    @GetMapping("/legacy-clients")
    public ResponseEntity<List<Client>> getLegacyClients() {
        return ResponseEntity.ok(service.getLegacyClients());
    }

    /**
     * Retrieves all clients that have been successfully migrated.
     *
     * @return List of migrated clients
     */
    @GetMapping("/new-clients")
    public ResponseEntity<List<Client>> getNewClients() {
        return ResponseEntity.ok(service.getNewClients());
    }

    /**
     * Migrates a client from the legacy system to the new product.
     *
     * @param id The ID of the client to migrate
     * @return ResponseEntity with success status
     */
    @PostMapping("/migrate/{id}")
    public ResponseEntity<Void> migrateClient(@PathVariable Long id) {
        service.migrateClient(id);
        return ResponseEntity.ok().build();
    }
}
