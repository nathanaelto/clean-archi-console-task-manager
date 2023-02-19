package org.esgi.domain.models;

import org.esgi.domain.models.dto.CreateTask;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

// todo implement subtask
// todo implement tags

public class Task {
    private final Integer id;
    private final String description;
    private final LocalDateTime creationDate;
    private final Optional<LocalDateTime> dueDate;
    private final Optional<LocalDateTime> closeDate;
    private final TaskState state;

    private static Clock clock = Clock.systemDefaultZone();

    public Task(Integer id, String description, LocalDateTime creationDate, Optional<LocalDateTime> dueDate, Optional<LocalDateTime> closeDate, TaskState state) {
        this.id = id;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.closeDate = closeDate;
        this.state = state;
    }

    public static void setLocalDateTime(Clock clock) {
        Task.clock = clock;
    }

    public final static Integer UNDEFINED_ID = -1;

    // todo ne plus passer par ça
    public Task(String description, LocalDateTime dueDate) {
        this(UNDEFINED_ID, description, LocalDateTime.now(clock), Optional.of(dueDate), TaskState.TODO, Optional.empty());
    }

    public Task(Integer id, String description, LocalDateTime creationDate, Optional<LocalDateTime> dueDate, TaskState state, Optional<LocalDateTime> closeDate) {
        this.id = id;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.state = state;
        this.closeDate = closeDate;
    }

    public static Task fromCreatedTask(CreateTask createTask) {
        return new Task(
                null,
                createTask.description,
                LocalDateTime.now(clock),
                createTask.dueDate,
                createTask.state.orElse(TaskState.TODO),
                Optional.empty()
        );
    }


    public Integer getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public Optional<LocalDateTime> getDueDate() {
        return dueDate;
    }

    public Optional<LocalDateTime> getCloseDate() {
        return closeDate;
    }

    public TaskState getState() {
        return state;
    }

    public Task cancelTask() {
        return new Task(this.id, this.description, this.creationDate, this.dueDate, TaskState.CANCELED, Optional.of(LocalDateTime.now(clock)));
    }

    public Task updateTaskId(Integer id) {
        return new Task(id, this.description, this.creationDate, this.dueDate, this.state, this.closeDate);
    }

    public Task updateTask(Optional<String> description, Optional<TaskState> state, Optional<LocalDateTime> dueDate) {
        Optional<LocalDateTime> closeDate = state.map(newState -> {
                    if (newState == TaskState.CLOSED || newState == TaskState.DONE || newState == TaskState.CANCELED) {
                        return LocalDateTime.now(clock);
                    }
                    return this.closeDate.orElse(null);
                }
        );
        return new Task(this.id, description.orElse(this.description), this.creationDate, Optional.ofNullable(dueDate.orElse(this.dueDate.orElse(null))), state.orElse(this.state), closeDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(description, task.description) && Objects.equals(creationDate, task.creationDate) && Objects.equals(dueDate, task.dueDate) && Objects.equals(closeDate, task.closeDate) && state == task.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, creationDate, dueDate, closeDate, state);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", dueDate=" + dueDate +
                ", closeDate=" + closeDate +
                ", state=" + state;
    }
}
