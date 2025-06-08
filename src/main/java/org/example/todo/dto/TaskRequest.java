package org.example.todo.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.todo.model.Status;

@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private Status status;
}
