package org.esgi;

import org.esgi.domain.services.TaskService;
import org.esgi.domain.exposition.UserInterface;
import org.esgi.domain.repository.ITaskRepository;
import org.esgi.domain.services.ITaskService;
import org.esgi.infrastructure.exposition.CLIInterface;
import org.esgi.infrastructure.repository.json.JsonFileRepository;
import org.esgi.infrastructure.repository.json.JsonRepository;

//todo faire le script pour lancer le programme
//todo faire le script pour lancer les tests
public class Main {
    public static void main(String[] args) {
        ITaskRepository taskRepository = new JsonRepository(new JsonFileRepository("./tasks.json"));
        ITaskService taskService = new TaskService(taskRepository);
        UserInterface userInterface = new CLIInterface(taskService);
        userInterface.processUserInputs(args);
    }
}
