package org.example.todo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.todo.model.Status;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}