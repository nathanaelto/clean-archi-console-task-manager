package org.esgi.domain.services;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;
import org.esgi.domain.repository.ITaskRepository;

import java.util.List;

// todo : exception custom
public class TaskService implements ITaskService {

    private final ITaskRepository repository;

    public TaskService(ITaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer addTask(CreateTask createTask) {
        Task task = Task.fromCreatedTask(createTask);
        return repository.add(task)
                .orElseThrow(() -> new RuntimeException("Task not added"));
    }

    @Override
    public void updateTask(UpdateTask updateTask) {
        Task task = repository.get(updateTask.id).orElseThrow(() -> new RuntimeException("Task not found"));
        task = task.updateTask(updateTask.description, updateTask.state, updateTask.dueDate);
        repository.update(task);
    }

    @Override
    public void removeTask(Integer id) {
        Task task = repository.get(id).orElseThrow();
        task = task.cancelTask();
        repository.update(task);
    }

    @Override
    public Task getOne(Integer id) {
        return repository.get(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public List<Task> getAllTask() {
        return repository.getAll();
    }
}
