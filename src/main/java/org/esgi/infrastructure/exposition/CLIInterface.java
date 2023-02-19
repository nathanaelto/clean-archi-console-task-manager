package org.esgi.infrastructure.exposition;

import org.esgi.domain.exposition.UserInterface;
import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.UpdateTask;
import org.esgi.domain.services.ITaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class CLIInterface implements UserInterface {

    private final String[] args;
    private final ITaskService taskService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

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

        CreateTask createTask = parseAdd();
        Integer id = taskService.addTask(createTask);
        System.out.println("Task added with id: " + id);
    }

    private CreateTask parseAdd() {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }

        CreateTask createTask = new CreateTask();
        int index = 1;
        while (index < args.length) {
            if (args[index].startsWith("-c"))
                createTask.description = parseContent(++index);
            if (args[index].startsWith("-d:"))
                createTask.dueDate = Optional.of(parseDate(index));
            index++;
        }

        return createTask;
    }

    public void parseListArguments() {
        if (args.length != 1) {
            throw new RuntimeException("Wrong argument number");
        }

        taskService.getAllTask()
                .stream()
                .sorted(Comparator.comparing(Task::getCreationDate))
                .toList()
                .forEach(System.out::println);
    }

    public void parseUpdateArguments() {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }

        Integer id = Integer.parseInt(args[1]);
        if (id < 0) {
            throw new RuntimeException("Wrong argument number");
        }

        UpdateTask updateTask = parseUpdate();
        updateTask.id = id;

        taskService.updateTask(updateTask);
    }

    private UpdateTask parseUpdate() {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }

        UpdateTask updateTask = new UpdateTask();
        int index = 2;
        while (index < args.length) {
            if (args[index].startsWith("-c"))
                updateTask.description = Optional.of(parseContent(++index));
            if (args[index].startsWith("-d:"))
                updateTask.dueDate = Optional.of(parseDate(index));
            if (args[index].startsWith("-s:"))
                updateTask.state = Optional.of(parseState(index));
            index++;
        }

        return updateTask;
    }

    public void parseRemoveArguments() {
        if (args.length != 2) {
            throw new RuntimeException("Wrong argument number");
        }
        Integer id = Integer.parseInt(args[1]);

        taskService.removeTask(id);
    }


    private LocalDateTime parseDate(int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return LocalDateTime.parse(args[i].substring(3), formatter);
    }

    private TaskState parseState(int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return switch (args[i].substring(3)) {
            case "todo" -> TaskState.TODO;
            case "progress" -> TaskState.IN_PROGRESS;
            case "done" -> TaskState.DONE;
            case "cancelled" -> TaskState.CANCELED;
            case "closed" -> TaskState.CLOSED;
            case "pending" -> TaskState.PENDING;
            default -> throw new RuntimeException("Unkwonw argument: " + args[0]);
        };
    }

    private String parseContent(int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return args[i];
    }
}
