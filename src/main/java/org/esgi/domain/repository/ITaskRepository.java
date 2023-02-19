package org.esgi.domain.repository;

import org.esgi.domain.models.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskRepository {

    Integer add(Task task);

    Optional<Task> get(Integer id);

    void update(Task updatedTask);

    List<Task> getAll();


}
