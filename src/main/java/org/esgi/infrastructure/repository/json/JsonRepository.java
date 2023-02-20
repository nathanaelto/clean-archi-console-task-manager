package org.esgi.infrastructure.repository.json;

import org.esgi.domain.models.Task;
import org.esgi.domain.repository.ITaskRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.esgi.domain.models.Task.UNDEFINED_ID;

/// TODO : remove all "e.printStackTrace();"
public class JsonRepository implements ITaskRepository {

    private final JsonFileRepository jsonFileRepository;

    public JsonRepository(JsonFileRepository jsonFileRepository) {
        this.jsonFileRepository = jsonFileRepository;
    }

    @Override
    public Optional<Integer> add(Task task) {
        try {
            List<JsonTask> tasks = jsonFileRepository.load();
            List<JsonTask> subTasks = tasks
                    .stream()
                    .flatMap(t -> t.getSubTasks().stream()).toList();
            List<JsonTask> allTasks = Stream.concat(tasks.stream(), subTasks.stream()).toList();
            int newId = allTasks.size();
            while (Objects.equals(task.getId(), UNDEFINED_ID)) {
                int finalNewId = newId;
                if (allTasks.stream().noneMatch(t -> Objects.equals(t.getId(), finalNewId))) {
                    break;
                }
                newId++;
            }
            Task newTask = task.updateTaskId(newId);
            if (newTask.getParentId().isPresent()) {
                Task newParentTask = get(newTask.getParentId().get()).orElseThrow();
                newParentTask = newParentTask.addSubTask(newTask);
                tasks = tasks.stream().filter(t -> !t.getId().equals(newTask.getParentId().get())).toList();
                tasks = Stream.concat(tasks.stream(), Stream.of(new JsonTask(newParentTask))).toList();
            } else {
                tasks = Stream.concat(tasks.stream(), Stream.of(new JsonTask(newTask))).toList();
            }
            jsonFileRepository.save(tasks);
            return Optional.of(newTask.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Task> get(Integer id) {
        try {
            List<JsonTask> tasks = jsonFileRepository.load();
            List<JsonTask> subTasks = tasks
                    .stream()
                    .flatMap(task -> task.getSubTasks().stream()).toList();
            tasks = Stream.concat(tasks.stream(), subTasks.stream()).toList();
            return tasks
                    .stream()
                    .filter(task -> Objects.equals(task.getId(), id))
                    .findFirst()
                    .map(JsonTask::toTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Task updatedTask) {
        try {
            List<JsonTask> tasks;
            if (updatedTask.getParentId().isPresent()) {
                tasks = jsonFileRepository.load()
                        .stream()
                        .filter(task -> !Objects.equals(task.getId(), updatedTask.getParentId().get()))
                        .toList();
                Task parent = get(updatedTask.getParentId().get()).orElseThrow();

                Task newParent = new Task(
                        parent.getId(),
                        parent.getDescription(),
                        parent.getCreationDate(),
                        parent.getDueDate(),
                        parent.getCloseDate(),
                        parent.getState(),
                        parent.getTag(),
                        Stream.concat(parent.getSubTasks().stream().filter(task -> !task.getId().equals(updatedTask.getId())), Stream.of(updatedTask)).toList(),
                        parent.getParentId()
                );
                tasks = Stream.concat(tasks.stream(), Stream.of(new JsonTask(newParent))).toList();
            } else {
                tasks = jsonFileRepository.load()
                        .stream()
                        .filter(task -> !Objects.equals(task.getId(), updatedTask.getId()))
                        .toList();
                tasks = Stream.concat(tasks.stream(), Stream.of(new JsonTask(updatedTask))).toList();
            }
            jsonFileRepository.save(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
