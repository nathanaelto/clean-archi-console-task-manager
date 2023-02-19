package org.esgi.domain.models;

import java.time.LocalDateTime;
import java.util.Optional;


//             todo     factory with localdatetime

public class Task {
    public final Integer id;
    public final String description;
    public final LocalDateTime creationDate;
    public final Optional<LocalDateTime> dueDate;
    public final Optional<LocalDateTime> closeDate;
    public final TaskState state;

    public final static Integer UNDEFINED_ID = -1;

    public Task(String description) {
        this(UNDEFINED_ID, description, LocalDateTime.now(), Optional.empty(), TaskState.TODO, Optional.empty());
    }

    public Task(String description, LocalDateTime dueDate) {
        this(UNDEFINED_ID, description, LocalDateTime.now(), Optional.of(dueDate), TaskState.TODO, Optional.empty());
    }

    public Task(Integer id, String description, LocalDateTime creationDate, Optional<LocalDateTime> dueDate, TaskState state, Optional<LocalDateTime> closeDate) {
        this.id = id;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.state = state;
        this.closeDate = closeDate;
    }

    public Task cancelTask() {
        return new Task(this.id, this.description, this.creationDate, this.dueDate, TaskState.CANCELED, closeDate);
    }

    public Task updateTaskId(Integer id) {
        return new Task(id, this.description, this.creationDate, this.dueDate, this.state, this.closeDate);
    }

    public Task updateTask(Optional<String> description, Optional<TaskState> state, Optional<LocalDateTime> dueDate) {
        Optional<LocalDateTime> closeDate = state.map(newState -> {
                    if (newState == TaskState.CLOSED || newState == TaskState.DONE || newState == TaskState.CANCELED) {
                        return LocalDateTime.now();
                    }
                    return this.closeDate.orElse(null);
                }
        );
        return new Task(this.id, description.orElse(this.description), this.creationDate, Optional.ofNullable(dueDate.orElse(this.dueDate.orElse(null))), state.orElse(this.state), closeDate);
    }
}
