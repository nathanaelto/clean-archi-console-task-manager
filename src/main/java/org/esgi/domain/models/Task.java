package org.esgi.domain.models;

import java.time.LocalDateTime;
import java.util.Optional;

public class Task {
    public Integer id;

    public String description;
    public final LocalDateTime creationDate;
    public Optional<LocalDateTime> dueDate;

    private LocalDateTime closeDate;
    private TaskState state;


    public Task(String description) {
        this.description = description;
        this.dueDate = Optional.empty();

        creationDate = LocalDateTime.now();
        state = TaskState.TODO;
    }

    public Task(String description, LocalDateTime dueDate) {
        this(description);
        this.dueDate = Optional.of(dueDate);
    }

    public void setState(TaskState state) {
        this.state = state;
        if(this.state == TaskState.CLOSED || this.state == TaskState.DONE || this.state == TaskState.CANCELED) {
            closeDate = LocalDateTime.now();
        }
    }

    public TaskState getState(){
        return state;
    }


    public LocalDateTime getCloseDate(){
        return closeDate;
    }

}
