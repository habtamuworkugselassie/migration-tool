package com.client.migration.controller;

import com.client.migration.model.Client;
import com.client.migration.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author habtamugebreselassie
 * Date: 06/01/2026
 * Time: 10:49
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping("/legacy-clients")
    public List<Client> legacyClients() {
        return service.getLegacyClients();
    }

    @GetMapping("/new-clients")
    public List<Client> newClients() {
        return service.getNewClients();
    }

    @PostMapping("/migrate/{id}")
    public ResponseEntity<?> migrate(@PathVariable Long id) {
        service.migrateClient(id);
        return ResponseEntity.ok().build();
    }
}
