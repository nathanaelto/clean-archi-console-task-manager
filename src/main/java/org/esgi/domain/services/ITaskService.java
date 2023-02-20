package org.esgi.domain.services;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;

import java.util.List;

public interface ITaskService {

    Integer addTask(CreateTask createTask);

    void updateTask(UpdateTask updateTask);

    void removeTask(Integer id);

    List<Task> getAllTasks();
}
