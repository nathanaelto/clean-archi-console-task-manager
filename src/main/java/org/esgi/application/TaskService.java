package org.esgi.application;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.esgi.domain.repository.ITaskRepository;
import org.esgi.domain.servcies.ITaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskService implements ITaskService {

    private final ITaskRepository repository;

    public TaskService(ITaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Integer addTask(Task task) {
        Integer id =  repository.add(task);
        if(id == null)
        {
            throw new RuntimeException("Error while adding task");
        }

        return task.id;
    }

    @Override
    public void updateTask(Integer id, Optional<String> description, Optional<TaskState> state, Optional<LocalDateTime> dueDate) {
        Task task = repository.get(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task = task.updateTask(description, state, dueDate);
        repository.update(task);
    }

    //todo call le update avec le statue "cancel"
    @Override
    public void removeTask(Integer id) {
        Task task = repository.get(id).orElseThrow();
        task = task.cancelTask();
        repository.update(task);
    }

    @Override
    public Task getOne(Integer id) {
        return repository.get(id).orElseThrow();
    }

    @Override
    public List<Task> getAllTask() {
        return repository.getAll();
    }
}
