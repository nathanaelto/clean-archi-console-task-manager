package org.esgi.infrastructure;

import org.esgi.domain.models.Task;
import org.esgi.domain.repository.ITaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JsonRepository implements ITaskRepository {

    private JsonFileRepository jsonFileRepository;
    private List<Task> tasks;

    public JsonRepository(JsonFileRepository jsonFileRepository) {
        this.jsonFileRepository = jsonFileRepository;
    }

    private void load() throws Exception {
        tasks = jsonFileRepository.load();
    }

    private void save() throws Exception {
        jsonFileRepository.save(tasks);
    }

    @Override
    public void add(Task task) {
        try {
            this.load();
            tasks.add(task);
            this.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<Task> get(Integer id) {
        try {
            this.load();
            for (Task task : tasks) {
                if (Objects.equals(task.id, id)) {
                    return Optional.of(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Task updatedTask) {
        try {
            this.load();
            for (Task task : tasks) {
                if (Objects.equals(task.id, updatedTask.id)) {
                    task = updatedTask;
                }
            }
            this.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getAll(){
        try {
            this.load();
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
