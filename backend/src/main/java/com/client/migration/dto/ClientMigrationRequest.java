package com.client.migration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for client migration request to new product.
 *
 * @author habtamugebreselassie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientMigrationRequest {
    @NotNull(message = "Client ID is required")
    private Long id;

    @NotBlank(message = "Client name is required")
    private String name;
}

