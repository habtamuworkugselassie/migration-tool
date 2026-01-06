package com.client.migration.exception;

/**
 * Exception thrown when a client is not found.
 *
 * @author habtamugebreselassie
 */
public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

