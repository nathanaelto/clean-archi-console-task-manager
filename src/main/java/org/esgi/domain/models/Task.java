package org.esgi.domain.models;

import java.time.LocalDateTime;
import java.util.Optional;

public class Task {
    public Integer id;

    public String description;
    public final LocalDateTime creationDate;
    public LocalDateTime dueDate;

    private LocalDateTime closeDate;
    private TaskState state;


    public Task(Integer id, String description, LocalDateTime dueDate, TaskState state) {
        this.description = description;
        this.id = id;
        this.dueDate = dueDate;
        this.state = state;
        creationDate = LocalDateTime.now();
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
