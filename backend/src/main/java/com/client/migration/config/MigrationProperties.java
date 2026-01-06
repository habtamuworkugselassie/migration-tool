package com.client.migration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author habtamugebreselassie
 * Configuration properties for migration tool
 */
@Component
@ConfigurationProperties(prefix = "migration")
public class MigrationProperties {
    private String newProductApiUrl;
    private boolean enableExternalMigration = true;
    private int timeoutSeconds = 30;

    public String getNewProductApiUrl() {
        return newProductApiUrl;
    }

    public void setNewProductApiUrl(String newProductApiUrl) {
        this.newProductApiUrl = newProductApiUrl;
    }

    public boolean isEnableExternalMigration() {
        return enableExternalMigration;
    }

    public void setEnableExternalMigration(boolean enableExternalMigration) {
        this.enableExternalMigration = enableExternalMigration;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}

