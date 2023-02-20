package org.esgi.domain.services.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Integer id) {
        super("Task with id " + id + " not found");
    }
}
