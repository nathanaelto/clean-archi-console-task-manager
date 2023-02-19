package org.esgi.infrastructure.repository.json;

import org.esgi.domain.models.Task;
import org.esgi.domain.repository.ITaskRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.esgi.domain.models.Task.UNDEFINED_ID;

public class JsonRepository implements ITaskRepository {

    private final JsonFileRepository jsonFileRepository;

    public JsonRepository(JsonFileRepository jsonFileRepository) {
        this.jsonFileRepository = jsonFileRepository;
    }

    @Override
    public Optional<Integer> add(Task task) {
        try {
            List<JsonTask> tasks = jsonFileRepository.load();
            int newId = tasks.size();
            while (Objects.equals(task.getId(), UNDEFINED_ID)) {
                int finalNewId = newId;
                if (tasks.stream().noneMatch(t -> Objects.equals(t.getId(), finalNewId))) {
                    break;
                }
                newId++;
            }
            tasks.add(new JsonTask(task.updateTaskId(newId)));
            jsonFileRepository.save(tasks);
            return Optional.of(task.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Task> get(Integer id) {
        try {
            return jsonFileRepository.load()
                    .stream()
                    .filter(task -> Objects.equals(task.getId(), id))
                    .findFirst()
                    .map(JsonTask::toTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //todo notifier d'un echec
    @Override
    public void update(Task updatedTask) {
        try {
            List<JsonTask> tasks = jsonFileRepository.load()
                    .stream()
                    .filter(task -> !Objects.equals(task.getId(), updatedTask.getId()))
                    .toList();

            jsonFileRepository.save(Stream.concat(tasks.stream(), Stream.of(new JsonTask(updatedTask))).toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo notifier d'un echec
    @Override
    public List<Task> getAll() {
        try {
            return jsonFileRepository.load()
                    .stream()
                    .map(JsonTask::toTask)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
