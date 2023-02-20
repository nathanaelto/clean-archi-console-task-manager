package org.esgi.domain.models.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class CreateTask {
    private final String description;
    private final Optional<LocalDateTime> dueDate;
    private final Optional<String> tag;
    private final Optional<Integer> parentId;

    private CreateTask(String description, Optional<LocalDateTime> dueDate, Optional<String> tag, Optional<Integer> parentId) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
        this.dueDate = dueDate;
        this.tag = tag;
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public Optional<LocalDateTime> getDueDate() {
        return dueDate;
    }

    public Optional<String> getTag() {
        return tag;
    }

    public Optional<Integer> getParentId() {
        return parentId;
    }

    public static CreateTaskBuilder builder() {
        return new CreateTaskBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTask that = (CreateTask) o;
        return Objects.equals(description, that.description) && Objects.equals(dueDate, that.dueDate) && Objects.equals(tag, that.tag) && Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, dueDate, tag, parentId);
    }

    public static class CreateTaskBuilder {
        private String description;
        private LocalDateTime dueDate;
        private String tag;
        private Integer parentId;

        public CreateTaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CreateTaskBuilder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public CreateTaskBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public CreateTaskBuilder parentId(Integer parentId) {
            this.parentId = parentId;
            return this;
        }

        public CreateTask build() {
            return new CreateTask(description, Optional.ofNullable(dueDate), Optional.ofNullable(tag), Optional.ofNullable(parentId));
        }
    }
}
