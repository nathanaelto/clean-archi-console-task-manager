package org.esgi.infrastructure.exposition;

import org.esgi.domain.exposition.UserInterface;
import org.esgi.domain.models.Task;
import org.esgi.domain.models.TaskState;
import org.esgi.domain.models.dto.CreateTask;
import org.esgi.domain.models.dto.CreateTask.CreateTaskBuilder;
import org.esgi.domain.models.dto.UpdateTask;
import org.esgi.domain.models.dto.UpdateTask.UpdateTaskBuilder;
import org.esgi.domain.services.ITaskService;
import org.esgi.infrastructure.exposition.exception.UnknownArgumentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class CLIInterface implements UserInterface {

    private final ITaskService taskService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CLIInterface(ITaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void processUserInputs(String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("Wrong argument number");
        }

        switch (args[0]) {
            case "add" -> parseAddArguments(args);
            case "list" -> parseListArguments(args);
            case "update" -> parseUpdateArguments(args);
            case "remove" -> parseRemoveArguments(args);
            default -> throw new UnknownArgumentException(args[0]);
        }
    }

    public void parseAddArguments(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }

        CreateTask createTask = parseAdd(args);
        Integer id = taskService.addTask(createTask);
        System.out.println("Task added with id: " + id);
    }

    private CreateTask parseAdd(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }

        CreateTaskBuilder createTaskBuilder = CreateTask.builder();
        int index = 1;
        while (index < args.length) {
            if (args[index].startsWith("-c"))
                createTaskBuilder.description(parseContent(args, ++index));
            if (args[index].startsWith("-d:"))
                createTaskBuilder.dueDate(parseDate(args, index));
            if (args[index].startsWith("-t"))
                createTaskBuilder.tag(parseContent(args, ++index));
            if (args[index].startsWith("-p"))
                createTaskBuilder.parentId(parseInteger(args, ++index));
            index++;
        }

        return createTaskBuilder.build();
    }

    public void parseListArguments(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("Wrong argument number");
        }

        taskService.getAllTasks()
                .stream()
                .sorted(Comparator.comparing(Task::getCreationDate)
                .reversed())
                .toList()
                .forEach(System.out::println);
    }

    public void parseUpdateArguments(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }

        int id = parseInteger(args, 1);
        if (id < 0) {
            throw new RuntimeException("Wrong argument number");
        }

        UpdateTask updateTask = parseUpdate(args, id);
        taskService.updateTask(updateTask);
    }

    private UpdateTask parseUpdate(String[] args, Integer id) {
        if (args.length < 3) {
            throw new RuntimeException("Wrong argument number");
        }

        UpdateTaskBuilder updateTaskBuilder = UpdateTask.builder();
        updateTaskBuilder.id(id);
        int index = 2;
        while (index < args.length) {
            if (args[index].startsWith("-c"))
                updateTaskBuilder.description(parseContent(args, ++index));
            if (args[index].startsWith("-d:"))
                updateTaskBuilder.dueDate(parseDate(args, index));
            if (args[index].startsWith("-s:"))
                updateTaskBuilder.state(parseState(args, index));
            if (args[index].startsWith("-t"))
                updateTaskBuilder.tag(parseContent(args, ++index));
            index++;
        }

        return updateTaskBuilder.build();
    }

    public void parseRemoveArguments(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("Wrong argument number");
        }
        Integer id = Integer.parseInt(args[1]);

        taskService.removeTask(id);
    }


    private LocalDateTime parseDate(String[] args, int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return LocalDate.parse(args[i].substring(3), formatter).atStartOfDay();
    }

    private TaskState parseState(String[] args, int i) {
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
            default -> throw new UnknownArgumentException(args[i].substring(3));
        };
    }

    private String parseContent(String[] args, int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return args[i];
    }

    private Integer parseInteger(String[] args, int i) {
        if (i >= args.length) {
            throw new RuntimeException("Wrong argument number");
        }

        return Integer.parseInt(args[i]);
    }
}
