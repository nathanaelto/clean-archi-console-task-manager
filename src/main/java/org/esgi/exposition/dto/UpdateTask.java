package org.esgi.exposition.dto;

import org.esgi.domain.models.TaskState;

import java.time.LocalDateTime;
import java.util.Optional;

public class UpdateTask {
    Integer id;
    Optional<String> description;
    Optional<LocalDateTime> dueDate;
    Optional<String> tag;
    Optional<TaskState> state;
}
