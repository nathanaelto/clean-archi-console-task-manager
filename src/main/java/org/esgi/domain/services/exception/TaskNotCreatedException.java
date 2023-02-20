package org.esgi.domain.services.exception;

public class TaskNotCreatedException extends RuntimeException{
    public TaskNotCreatedException() {
        super("Task not created");
    }
}
