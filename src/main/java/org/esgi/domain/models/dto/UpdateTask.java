package org.esgi.domain.models.dto;

import org.esgi.domain.models.TaskState;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class UpdateTask {
    private final Integer id;
    private final Optional<String> description;
    private final Optional<LocalDateTime> dueDate;
    private final Optional<TaskState> state;
    private final Optional<String> tag;

    private UpdateTask(Integer id, Optional<String> description, Optional<TaskState> state, Optional<LocalDateTime> dueDate, Optional<String> tag) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.state = state;
        this.tag = tag;
    }


    public Integer getId() {
        return id;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Optional<LocalDateTime> getDueDate() {
        return dueDate;
    }

    public Optional<TaskState> getState() {
        return state;
    }

    public Optional<String> getTag() {
        return tag;
    }

    public static UpdateTaskBuilder builder() {
        return new UpdateTaskBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateTask that = (UpdateTask) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(dueDate, that.dueDate) && Objects.equals(state, that.state) && Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, dueDate, state, tag);
    }

    public static class UpdateTaskBuilder {
        private Integer id;
        private String description;
        private LocalDateTime dueDate;
        private String tag;
        private TaskState state;

        public UpdateTaskBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UpdateTaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public UpdateTaskBuilder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public UpdateTaskBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public UpdateTaskBuilder state(TaskState state) {
            this.state = state;
            return this;
        }

        public UpdateTask build() {
            return new UpdateTask(id, Optional.ofNullable(description), Optional.ofNullable(state), Optional.ofNullable(dueDate), Optional.ofNullable(tag));
        }
    }
}

