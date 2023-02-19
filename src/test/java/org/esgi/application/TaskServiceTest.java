package org.esgi.application;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;
import org.esgi.domain.repository.ITaskRepository;
import org.esgi.domain.services.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.Mockito.*;

//todo : end to end test
//todo : export la notion du temps pour que ce soit testable
//todo : verifier toutes les exceptions

public class TaskServiceTest {

    private TaskService taskService;
    private ITaskRepository repository;

    @BeforeEach
    void setup() {
        repository = mock(ITaskRepository.class);
        taskService = new TaskService(repository);
    }

    @Test
    void should_call_the_add_repository_when_creating_task() {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        when(repository.add(any())).thenReturn(Optional.of(1));
        CreateTask createdTask = new CreateTask("test", Optional.of(LocalDateTime.now(clock)));

        taskService.addTask(createdTask);
        Task task = Task.fromCreatedTask(createdTask);
        verify(repository, times(1)).add(task);
    }

    @Test
    void should_throw_an_exception_when_can_t_create_task() {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        when(repository.add(any())).thenReturn(Optional.empty());
        CreateTask createdTask = new CreateTask("test", Optional.of(LocalDateTime.now(clock)));

        Assertions.assertThrows(RuntimeException.class, () -> {
            taskService.addTask(createdTask);
        });

        Task task = Task.fromCreatedTask(createdTask);
        verify(repository, times(1)).add(task);
    }

    @Test
    void should_call_the_update_task_when_updating_task() {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        Task task = new Task(
                1,
                "test",
                LocalDateTime.of(2020, 1, 1, 1, 1),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty()
        );
        when(repository.get(1)).thenReturn(Optional.of(task));

        UpdateTask updateTask = new UpdateTask(
                1,
                Optional.of("test 2"),
                Optional.of(TaskState.DONE),
                Optional.empty()
        );
        taskService.updateTask(updateTask);

        verify(repository, times(1)).get(1);
        Task updatedTask = new Task(
                1,
                "test 2",
                LocalDateTime.of(2020, 1, 1, 1, 1),
                Optional.empty(),
                TaskState.DONE,
                Optional.of(LocalDateTime.now(clock))
        );
        verify(repository, times(1)).update(updatedTask);
    }

    @Test
    void should_throw_exception_when_updating_task_that_does_not_exist() {
        when(repository.get(anyInt())).thenReturn(Optional.empty());

        UpdateTask updateTask = new UpdateTask(
                1,
                Optional.of("test 2"),
                Optional.of(TaskState.DONE),
                Optional.of(LocalDateTime.now())
        );

        Assertions.assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(updateTask);
        });
        verify(repository, times(1)).get(1);
        verify(repository, times(0)).update(any());
    }

    @Test
    void should_call_the_update_task_when_deleting_task() {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        Task task = new Task(
                1,
                "test",
                LocalDateTime.of(2020, 1, 1, 1, 1),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty()
        );
        when(repository.get(1)).thenReturn(Optional.of(task));

        taskService.removeTask(1);

        verify(repository, times(1)).get(1);
        Task updatedTask = new Task(
                1,
                "test",
                LocalDateTime.of(2020, 1, 1, 1, 1),
                Optional.empty(),
                TaskState.CANCELED,
                Optional.of(LocalDateTime.now(clock))
        );
        verify(repository, times(1)).update(updatedTask);
    }

    @Test
    void should_call_the_get_task_when_get_single_task() {

    }

    @Test
    void should_call_get_all_task_when_get_all_tasks() {

    }
}
