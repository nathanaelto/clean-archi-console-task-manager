package org.esgi.application;

import org.esgi.domain.repository.ITaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

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
        //taskService.addTask();
        verify(repository, times(1)).add(any());
    }

    @Test
    void should_call_the_update_task_when_updating_task() {

    }

    @Test
    void should_call_the_update_task_when_deleting_task() {

    }

    @Test
    void should_call_the_get_task_when_get_single_task() {

    }

    @Test
    void should_call_get_all_task_when_get_all_tasks() {

    }
}
