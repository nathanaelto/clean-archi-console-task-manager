package org.esgi.application;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.esgi.domain.repository.ITaskRepository;
import org.esgi.domain.servcies.ITaskService;

import java.util.List;

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
    public void updateTask(Task task) {
        repository.update(task);
    }

    //todo call le update avec le statue "cancel"
    @Override
    public void removeTask(Integer id) {
        Task task = repository.get(id).orElseThrow();
        task.setState(TaskState.CANCELED);
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
