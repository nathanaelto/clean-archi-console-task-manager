package org.esgi.exposition;

import org.esgi.domain.exposition.UserInterface;
import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.esgi.domain.servcies.ITaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CLIInterface implements UserInterface {

    private final String[] args;
    private final ITaskService taskService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
    private LocalDateTime dueDate;
    private TaskState state;
    private String content;

    public CLIInterface(ITaskService taskService, String[] args) {
        this.taskService = taskService;
        this.args = args;
    }

    @Override
    public void processUserInputs() {
        if (args.length < 1) {
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
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }
        parseArgumentOptions(1);

        Task task = new Task(content, dueDate);
        Integer id = taskService.addTask(task);
        System.out.println("Task added with id: " + id);
    }

    public void parseListArguments() {
        if (args.length != 1) {
            throw new RuntimeException("Wrong argument number");
        }

        taskService.getAllTask();
    }

    public void parseUpdateArguments() {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }
        Integer id = Integer.parseInt(args[1]);

        parseArgumentOptions(2);
        taskService.updateTask(id, Optional.ofNullable(content), Optional.ofNullable(state), Optional.ofNullable(dueDate));
    }

    public void parseRemoveArguments() {
        if (args.length != 2) {
            throw new RuntimeException("Wrong argument number");
        }
        Integer id = Integer.parseInt(args[1]);

        taskService.removeTask(id);
    }


    private void parseArgumentOptions(int start) {
        for (int i = start; i < args.length; i+=2) {
            parseArgumentOption(i);
        }
    }

    private void parseArgumentOption(int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        switch (args[i]) {
            case "-d" -> dueDate = parseDate(i + 1);
            case "-s" -> state = parseState(i + 1);
            case "-c" -> content = parseContent(i + 1);
            default -> throw new RuntimeException("Unkwonw argument: " + args[0]);
        }
    }

    private LocalDateTime parseDate(int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return LocalDateTime.parse(args[i], formatter);
    }

    private TaskState parseState(int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return TaskState.valueOf(args[i]);
    }

    private String parseContent(int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return args[i];
    }

}
