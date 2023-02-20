package org.esgi.domain.models;

import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Task {
    private final Integer id;
    private final String description;
    private final LocalDateTime creationDate;
    private final Optional<LocalDateTime> dueDate;
    private final Optional<LocalDateTime> closeDate;
    private final TaskState state;
    private final Optional<String> tag;
    private final List<Task> subTasks;
    private final Optional<Integer> parentId;

    private static Clock clock = Clock.systemDefaultZone();

    public final static Integer UNDEFINED_ID = -1;

    public static void setLocalDateTime(Clock clock) {
        Task.clock = clock;
    }

    public Task(Integer id, String description, LocalDateTime creationDate, Optional<LocalDateTime> dueDate, Optional<LocalDateTime> closeDate, TaskState state, Optional<String> tag, List<Task> subTasks, Optional<Integer> parentId) {
        this.id = id;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.closeDate = closeDate;
        this.state = state;
        this.tag = tag;
        this.subTasks = subTasks;
        this.parentId = parentId;
    }

    public static Task fromCreatedTask(CreateTask createTask) {
        return new Task(null, createTask.getDescription(), LocalDateTime.now(clock), createTask.getDueDate(), Optional.empty(), TaskState.TODO, createTask.getTag(), List.of(), createTask.getParentId());
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

    public Optional<String> getTag() {
        return tag;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

    public Optional<Integer> getParentId() {
        return parentId;
    }

    public Task cancel() {
        return new Task(this.id, this.description, this.creationDate, this.dueDate, Optional.of(LocalDateTime.now(clock)), TaskState.CANCELED, this.tag, this.subTasks, this.parentId);
    }

    public Task updateTaskId(Integer id) {
        return new Task(id, this.description, this.creationDate, this.dueDate, this.closeDate, this.state, this.tag, this.subTasks, this.parentId);
    }

    public Task updateTask(UpdateTask updateTask) {
        Optional<LocalDateTime> closeDate = updateTask.getState().map(newState -> {
            if (newState == TaskState.CLOSED || newState == TaskState.DONE || newState == TaskState.CANCELED) {
                return LocalDateTime.now(clock);
            }
            return this.closeDate.orElse(null);
        });
        return new Task(
                this.id,
                updateTask.getDescription().orElse(this.description),
                this.creationDate,
                Optional.ofNullable(updateTask.getDueDate().orElse(this.dueDate.orElse(null))),
                closeDate,
                updateTask.getState().orElse(this.state),
                Optional.ofNullable(updateTask.getTag().orElse(this.tag.orElse(null))),
                this.subTasks,
                this.parentId
        );
    }

    public Task addSubTask(Task task) {
        List<Task> newSubTasks = Stream.concat(this.subTasks.stream(), Stream.of(task)).toList();
        return new Task(this.id, this.description, this.creationDate, this.dueDate, this.closeDate, this.state, this.tag, newSubTasks, this.parentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(description, task.description) && Objects.equals(creationDate, task.creationDate) && Objects.equals(dueDate, task.dueDate) && Objects.equals(closeDate, task.closeDate) && state == task.state && Objects.equals(tag, task.tag) && Objects.equals(subTasks, task.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, creationDate, dueDate, closeDate, state, tag, subTasks);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", dueDate=" + dueDate +
                ", closeDate=" + closeDate +
                ", state=" + state +
                ", tag=" + tag +
                "\n\tsubTasks=" + subTasks;
    }
}
