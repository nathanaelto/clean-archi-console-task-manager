package org.esgi.domain.models;

import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void should_create_a_task() {
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );

        assertEquals(0, task.getId());
        assertEquals("test", task.getDescription());
        assertEquals(TaskState.TODO, task.getState());
    }

    @Test
    void should_create_a_task_from_created_task() {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        CreateTask createTask = CreateTask.builder()
                .description("test")
                .dueDate(LocalDateTime.of(2025, 1, 1, 1, 1))
                .build();

        Task task = Task.fromCreatedTask(createTask);

        assertEquals("test", task.getDescription());
        assertEquals(TaskState.TODO, task.getState());
        assertEquals(Optional.of(LocalDateTime.of(2025, 1, 1, 1, 1)), task.getDueDate());
        assertEquals(LocalDateTime.of(2020, 1, 1, 1, 1), task.getCreationDate());
    }

    @Test
    void should_cancel_a_task() {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );

        Task newTask = task.cancel();

        assertEquals(TaskState.CANCELED, newTask.getState());
        assertEquals(Optional.of(LocalDateTime.of(2020, 1, 1, 1, 1)), newTask.getCloseDate());
        assertEquals("test", newTask.getDescription());
    }

    @Test
    void should_update_a_task_id() {
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );

        Task newTask = task.updateTaskId(1);

        assertEquals(1, newTask.getId());
    }

    @Test
    void should_update_a_task() {
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );

        UpdateTask updateTask = UpdateTask.builder()
                .description("test2")
                .state(TaskState.DONE)
                .build();
        Task newTask = task.updateTask(updateTask);

        assertEquals("test2", newTask.getDescription());
        assertEquals(TaskState.DONE, newTask.getState());
        assertEquals(0, newTask.getId());
    }

    @Test
    void should_add_a_subtask() {
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );

        Task subTask = new Task(
                1,
                "test2",
                LocalDateTime.now(),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.of(0)
        );

        Task newTask = task.addSubTask(subTask);

        assertEquals(1, newTask.getSubTasks().size());
        assertEquals(1, newTask.getSubTasks().get(0).getId());
    }
}
