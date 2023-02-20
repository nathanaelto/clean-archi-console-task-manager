package org.esgi.domain.services;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;
import org.esgi.domain.repository.ITaskRepository;
import org.esgi.domain.services.exception.TaskNotCreatedException;
import org.esgi.domain.services.exception.TaskNotFoundException;

import java.util.List;

public class TaskService implements ITaskService {

    private final ITaskRepository repository;

    public TaskService(ITaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer addTask(CreateTask createTask) {
        Task task = Task.fromCreatedTask(createTask);
        return repository.add(task)
                    .orElseThrow(TaskNotCreatedException::new);
    }

    @Override
    public void updateTask(UpdateTask updateTask) {
        Task task = repository.get(updateTask.getId()).orElseThrow(() -> new TaskNotFoundException(updateTask.getId()));
        task = task.updateTask(updateTask);
        repository.update(task);
    }

    @Override
    public void removeTask(Integer id) {
        Task task = repository.get(id).orElseThrow(() -> new TaskNotFoundException(id));
        task = task.cancel();
        repository.update(task);
    }

    /// TODO : Sort by date (more recent first)
    @Override
    public List<Task> getAllTasks() {
        return repository.getAll();
    }
}
