package org.esgi;

import org.esgi.application.TaskService;
import org.esgi.domain.exposition.UserInterface;
import org.esgi.domain.repository.ITaskRepository;
import org.esgi.domain.servcies.ITaskService;
import org.esgi.exposition.CLIInterface;
import org.esgi.infrastructure.JsonFileRepository;
import org.esgi.infrastructure.JsonRepository;

public class Main {
    public static void main(String[] args) throws Exception {
        ITaskRepository taskRepository = new JsonRepository(new JsonFileRepository("./tasks.json"));
        ITaskService taskService = new TaskService(taskRepository);
        UserInterface userInterface = new CLIInterface(taskService, args);
        userInterface.processUserInputs();

    }
}
