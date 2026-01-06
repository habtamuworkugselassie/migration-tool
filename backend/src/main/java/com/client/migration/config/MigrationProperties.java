package com.client.migration.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the migration tool.
 * These properties can be configured via application.yaml or environment variables.
 *
 * @author habtamugebreselassie
 */
@Data
@Component
@ConfigurationProperties(prefix = "migration")
public class MigrationProperties {
    private String newProductApiUrl;
    private boolean enableExternalMigration = true;
    private int timeoutSeconds = 30;
}
