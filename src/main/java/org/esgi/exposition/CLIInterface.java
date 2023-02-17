package org.esgi.exposition;

import org.esgi.application.TaskService;
import org.esgi.domain.exposition.UserInterface;

public class CLIInterface implements UserInterface {

    private final String[] args;
    private final TaskService taskService;

    public CLIInterface(TaskService taskService, String[] args) {
        this.taskService = taskService;
        this.args = args;
    }

    @Override
    public void processUserInputs()
    {
        if(args.length < 1) {
            throw new RuntimeException("Wrong argument number");
        }

        switch (args[0]) {
            case "add" -> parseAddArguments();
            case "list" -> parseListArguments();
            case "update" -> parseUpdateArguments();
            case "remove" -> parseRemoveArguments();
            default -> throw new RuntimeException("Unkwonw argument: " + args[0]);
        }
    }

    public void parseAddArguments() {
        if(args.length < 1) {
            throw new RuntimeException("Wrong argument number");
        }

        taskService.addTask(null);
    }

    public void parseListArguments() {
        if(args.length > 1) {
            throw new RuntimeException("Wrong argument number");
        }

        taskService.getAllTask();
    }

    public void parseUpdateArguments() {
        if(args.length < 1) {
            throw new RuntimeException("Wrong argument number");
        }

        taskService.updateTask(null);
    }

    public void parseRemoveArguments() {
        if(args.length < 1) {
            throw new RuntimeException("Wrong argument number");
        }

        taskService.removeTask(null);
    }

}
