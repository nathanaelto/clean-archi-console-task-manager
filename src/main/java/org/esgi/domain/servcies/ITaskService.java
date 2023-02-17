package org.esgi.domain.servcies;

import org.esgi.domain.models.Task;

import java.util.List;

public interface ITaskService {

    void addTask(Task task);

    void updateTask(Task task);

    void removeTask(Task task);
    
    Task getOne(Integer id);

    List<Task> getAllTask();
}
