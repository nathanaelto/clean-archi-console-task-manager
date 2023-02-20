package org.esgi.infrastructure.repository.json;


import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JsonTask {
    private final Integer id;
    private final String description;
    private final LocalDateTime creationDate;
    private final LocalDateTime dueDate;
    private final LocalDateTime closeDate;
    private final TaskState state;
    private final String tag;
    private final JsonTask[] subTasks;
    private final Integer parentId;

    public JsonTask(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.creationDate = task.getCreationDate();
        this.dueDate = task.getDueDate().orElse(null);
        this.closeDate = task.getCloseDate().orElse(null);
        this.state = task.getState();
        this.tag = task.getTag().orElse(null);
        this.subTasks = task.getSubTasks().stream().map(JsonTask::new).toArray(JsonTask[]::new);
        this.parentId = task.getParentId().orElse(null);
    }

    public Task toTask() {
        return new Task(id, description, creationDate, Optional.ofNullable(dueDate), Optional.ofNullable(closeDate), state, Optional.ofNullable(tag), Arrays.stream(subTasks).map(JsonTask::toTask).toList(), Optional.ofNullable(parentId));
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonTask jsonTask = (JsonTask) o;
        return Objects.equals(id, jsonTask.id) && Objects.equals(description, jsonTask.description) && Objects.equals(creationDate, jsonTask.creationDate) && Objects.equals(dueDate, jsonTask.dueDate) && Objects.equals(closeDate, jsonTask.closeDate) && state == jsonTask.state && Objects.equals(tag, jsonTask.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, creationDate, dueDate, closeDate, state, tag);
    }

    public List<JsonTask> getSubTasks() {
        return Arrays.asList(subTasks);
    }
}
