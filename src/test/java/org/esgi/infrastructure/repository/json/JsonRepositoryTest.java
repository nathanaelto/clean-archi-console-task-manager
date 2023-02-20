package org.esgi.infrastructure.repository.json;

import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.esgi.domain.models.Task.UNDEFINED_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonRepositoryTest {

    private JsonRepository jsonRepository;
    private JsonFileRepository jsonFileRepository;

    @BeforeEach
    void setUp() {
        jsonFileRepository = mock(JsonFileRepository.class);
        jsonRepository = new JsonRepository(jsonFileRepository);
    }

    @Test
    void should_add_a_task() throws IOException {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        when(jsonFileRepository.load()).thenReturn(List.of());
        Task task = new Task(
                UNDEFINED_ID,
                "test",
                LocalDateTime.now(clock),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );

        Optional<Integer> id = jsonRepository.add(task);

        verify(jsonFileRepository).save(List.of(
                new JsonTask(task.updateTaskId(0))
        ));
        assertTrue(id.isPresent());
        assertEquals(0, id.get());
    }

    @Test
    void should_get_task_by_id() throws IOException {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(clock),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );
        when(jsonFileRepository.load()).thenReturn(List.of(
                new JsonTask(task)
        ));

        Optional<Task> taskById = jsonRepository.get(0);

        assertTrue(taskById.isPresent());
        assertEquals(task, taskById.get());
        assertEquals(0, taskById.get().getId());
        assertEquals("test", taskById.get().getDescription());
        assertEquals(LocalDateTime.now(clock), taskById.get().getCreationDate());
        assertEquals(TaskState.TODO, taskById.get().getState());
    }

    @Test
    void should_get_empty_when_task_not_found() throws IOException {
        when(jsonFileRepository.load()).thenReturn(List.of());

        Optional<Task> taskById = jsonRepository.get(0);

        assertTrue(taskById.isEmpty());
    }

    @Test
    void should_update_task() throws IOException {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(clock),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );
        when(jsonFileRepository.load()).thenReturn(List.of(
                new JsonTask(task)
        ));

        Task updatedTask = new Task(
                0,
                "test 2",
                LocalDateTime.now(clock),
                Optional.empty(),
                Optional.empty(),
                TaskState.DONE,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );
        jsonRepository.update(updatedTask);

        verify(jsonFileRepository).save(List.of(
                new JsonTask(updatedTask)
        ));
        assertEquals(0, updatedTask.getId());
        assertEquals("test 2", updatedTask.getDescription());
        assertEquals(LocalDateTime.now(clock), updatedTask.getCreationDate());
        assertEquals(TaskState.DONE, updatedTask.getState());
    }

    @Test
    void should_get_all_tasks() throws IOException {
        Clock clock = Clock.fixed(
                LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        Task.setLocalDateTime(clock);
        Task task = new Task(
                0,
                "test",
                LocalDateTime.now(clock),
                Optional.empty(),
                Optional.empty(),
                TaskState.TODO,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );
        Task task2 = new Task(
                0,
                "test 2",
                LocalDateTime.now(clock),
                Optional.of(LocalDateTime.of(2023, 1, 1, 1, 1)),
                Optional.empty(),
                TaskState.DONE,
                Optional.empty(),
                List.of(),
                Optional.empty()
        );
        when(jsonFileRepository.load()).thenReturn(List.of(
                new JsonTask(task), new JsonTask(task2)
        ));

        List<Task> tasks = jsonRepository.getAll();

        assertEquals(2, tasks.size());
        assertEquals(0, tasks.get(0).getId());
        assertEquals("test", tasks.get(0).getDescription());
        assertEquals(LocalDateTime.now(clock), tasks.get(0).getCreationDate());
        assertEquals(TaskState.TODO, tasks.get(0).getState());
        assertEquals(0, tasks.get(1).getId());
        assertEquals("test 2", tasks.get(1).getDescription());
        assertEquals(LocalDateTime.now(clock), tasks.get(1).getCreationDate());
        assertEquals(TaskState.DONE, tasks.get(1).getState());
    }
}
