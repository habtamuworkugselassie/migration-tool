package com.client.migration.exception;

/**
 * Exception thrown when client migration fails.
 *
 * @author habtamugebreselassie
 */
public class MigrationException extends RuntimeException {
    public MigrationException(String message) {
        super(message);
    }

    public MigrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

