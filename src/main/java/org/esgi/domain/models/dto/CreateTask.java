package org.esgi.domain.models.dto;

import org.esgi.domain.models.TaskState;

import java.time.LocalDateTime;
import java.util.Optional;

public class CreateTask {
    public String description;
    public Optional<LocalDateTime> dueDate = Optional.empty();
    public Optional<String> tag = Optional.empty();
    public Optional<TaskState> state = Optional.empty();

    public CreateTask() {
    }

    public CreateTask(String description, Optional<LocalDateTime> dueDate) {
        this.description = description;
        this.dueDate = dueDate;
    }
}