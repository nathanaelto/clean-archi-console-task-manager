package org.esgi.domain.models.dto;

import org.esgi.domain.models.TaskState;

import java.time.LocalDateTime;
import java.util.Optional;

public class UpdateTask {
    public Integer id;
    public Optional<String> description = Optional.empty();
    public Optional<LocalDateTime> dueDate = Optional.empty();
    public Optional<String> tag = Optional.empty();
    public Optional<TaskState> state = Optional.empty();

    public UpdateTask() {
    }

    public UpdateTask(Integer id, Optional<String> description, Optional<TaskState> state, Optional<LocalDateTime> dueDate) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.state = state;
    }
}

