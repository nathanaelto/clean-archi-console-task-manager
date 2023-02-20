package org.esgi.infrastructure.exposition;

import org.esgi.domain.models.TaskState;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;
import org.esgi.domain.services.ITaskService;
import org.esgi.infrastructure.exposition.exception.UnknownArgumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CLIInterfaceTest {

    private ITaskService taskService;
    private CLIInterface cliInterface;


    @BeforeEach
    void setUp() {
        taskService = mock(ITaskService.class);
        cliInterface = new CLIInterface(taskService);
    }

    @Test
    void should_throw_exception_when_not_providing_a_correct_argument() {
        String[] args = {"wrong"};
        Assertions.assertThrows(UnknownArgumentException.class, () -> cliInterface.processUserInputs(args));
    }

    @Test
    void should_create_a_task() {
        String[] args = {"add", "-c", "test"};
        cliInterface.processUserInputs(args);

        CreateTask createTask = CreateTask.builder()
                .description("test")
                .build();
        verify(taskService).addTask(createTask);
    }

    @Test
    void should_create_a_task_with_a_tag() {
        String[] args = {"add", "-c", "test", "-t", "tag"};
        cliInterface.processUserInputs(args);

        CreateTask createTask = CreateTask.builder()
                .description("test")
                .tag("tag")
                .build();
        verify(taskService).addTask(createTask);
    }

    @Test
    void should_create_a_task_with_a_parent_id() {
        String[] args = {"add", "-c", "test", "-p", "1"};
        cliInterface.processUserInputs(args);

        CreateTask createTask = CreateTask.builder()
                .description("test")
                .parentId(1)
                .build();
        verify(taskService).addTask(createTask);
    }

    @Test
    void should_create_a_task_with_a_due_date() {
        String[] args = {"add", "-c", "test", "-d:2021-01-01"};
        cliInterface.processUserInputs(args);

        CreateTask createTask = CreateTask.builder()
                .description("test")
                .dueDate(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        verify(taskService).addTask(createTask);
    }

    @Test
    void should_throw_error_when_create_a_task_without_description() {
        String[] args = {"add", "-d:2021-01-01-00-00"};

        Assertions.assertThrows(RuntimeException.class, () -> cliInterface.processUserInputs(args));
    }

    @Test
    void should_update_the_description_of_a_task() {
        String[] args = {"update", "1", "-c", "test"};
        cliInterface.processUserInputs(args);

        UpdateTask updateTask = UpdateTask.builder()
                .id(1)
                .description("test")
                .build();
        verify(taskService).updateTask(updateTask);
    }

    @Test
    void should_update_the_tag_of_a_task() {
        String[] args = {"update", "1", "-t", "tag"};
        cliInterface.processUserInputs(args);

        UpdateTask updateTask = UpdateTask.builder()
                .id(1)
                .tag("tag")
                .build();
        verify(taskService).updateTask(updateTask);
    }

    @Test
    void should_update_the_due_date_of_a_task() {
        String[] args = {"update", "1", "-d:2021-01-01"};
        cliInterface.processUserInputs(args);

        UpdateTask updateTask = UpdateTask.builder()
                .id(1)
                .dueDate(LocalDateTime.of(2021, 1, 1, 0, 0))
                .build();
        verify(taskService).updateTask(updateTask);
    }

    @Test
    void should_update_the_state_of_a_task() {
        String[] args = {"update", "1", "-s:done"};
        cliInterface.processUserInputs(args);

        UpdateTask updateTask = UpdateTask.builder()
                .id(1)
                .state(TaskState.DONE)
                .build();
        verify(taskService).updateTask(updateTask);
    }

    @Test
    void should_cancel_a_task() {
        String[] args = {"remove", "1"};
        cliInterface.processUserInputs(args);

        verify(taskService).removeTask(1);
    }

    @Test
    void should_get_all_task() {
        String[] args = {"list"};
        cliInterface.processUserInputs(args);

        verify(taskService).getAllTasks();
    }
}
