package org.esgi.domain.services;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITaskService {

    Integer addTask(CreateTask createTask);

    void updateTask(UpdateTask updateTask);

    void removeTask(Integer id);
    
    Task getOne(Integer id);

    List<Task> getAllTask();
}
