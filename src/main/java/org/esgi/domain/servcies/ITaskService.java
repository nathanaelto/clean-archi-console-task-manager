package org.esgi.domain.servcies;

import org.esgi.domain.models.Task;

import java.util.List;

public interface ITaskService {

    Integer addTask(Task task);

    void updateTask(Task task);

    void removeTask(Integer id);
    
    Task getOne(Integer id);

    List<Task> getAllTask();
}
