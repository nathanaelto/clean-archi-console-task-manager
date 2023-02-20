package org.esgi.infrastructure.exposition.exception;

public class UnknownArgumentException extends RuntimeException {
    public UnknownArgumentException(String argument) {
        super("Unknown argument: " + argument);
    }
}
