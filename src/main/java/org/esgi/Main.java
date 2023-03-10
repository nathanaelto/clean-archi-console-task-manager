package org.esgi;

import org.esgi.domain.services.TaskService;
import org.esgi.domain.exposition.UserInterface;
import org.esgi.domain.repository.ITaskRepository;
import org.esgi.domain.services.ITaskService;
import org.esgi.infrastructure.exposition.CLIInterface;
import org.esgi.infrastructure.repository.json.JsonFileRepository;
import org.esgi.infrastructure.repository.json.JsonRepository;

import java.nio.file.FileSystems;

public class Main {
    public static void main(String[] args) {
        String userDirectory = System.getProperty("user.home");
        String storageFile = ".consoleagenda/data.json";
        String storagePath = userDirectory + FileSystems.getDefault().getSeparator() + storageFile;

        ITaskRepository taskRepository = new JsonRepository(new JsonFileRepository(storagePath));
        ITaskService taskService = new TaskService(taskRepository);
        UserInterface userInterface = new CLIInterface(taskService);
        userInterface.processUserInputs(args);
    }
}
