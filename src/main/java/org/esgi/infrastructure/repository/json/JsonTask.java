package org.esgi.infrastructure.repository.json;


import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;

import java.time.LocalDateTime;
import java.util.Optional;

//todo : faire les dto pour les json
//todo : faire les mappers pour les json
public class JsonTask {
    private final Integer id;
    private final String description;
    private final LocalDateTime creationDate;
    private final LocalDateTime dueDate;
    private final LocalDateTime closeDate;
    private final TaskState state;

    public JsonTask(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.creationDate = task.getCreationDate();
        this.dueDate = task.getDueDate().orElse(null);
        this.closeDate = task.getCloseDate().orElse(null);
        this.state = task.getState();
    }

    public Task toTask() {
        return new Task(id, description, creationDate, Optional.ofNullable(dueDate), state, Optional.ofNullable(closeDate));
    }

    public Integer getId() {
        return id;
    }
}
