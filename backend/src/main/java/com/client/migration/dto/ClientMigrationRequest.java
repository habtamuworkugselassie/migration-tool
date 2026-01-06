package com.client.migration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author habtamugebreselassie
 * DTO for client migration request to new product
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientMigrationRequest {
    private Long id;
    private String name;
}

