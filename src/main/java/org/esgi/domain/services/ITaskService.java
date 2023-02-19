package org.esgi.domain.services;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITaskService {

    Integer addTask(Task task);

    void updateTask(Integer id, Optional<String> description, Optional<TaskState> state, Optional<LocalDateTime> dueDate);

    void removeTask(Integer id);
    
    Task getOne(Integer id);

    List<Task> getAllTask();
}
