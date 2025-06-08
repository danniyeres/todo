package org.example.todo.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.todo.dto.TaskRequest;
import org.example.todo.dto.TaskResponse;
import org.example.todo.model.Status;
import org.example.todo.model.Task;
import org.example.todo.model.User;
import org.example.todo.repository.UserRepository;
import org.example.todo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        var user = getCurrentUser();

        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .user(user)
                .build();

        return ResponseEntity.ok(toResponse(taskService.createTask(task)));
    }

    @GetMapping("/get")
    public ResponseEntity<List<TaskResponse>> getTasks(@RequestParam(required = false) Status status) {
        var user = getCurrentUser();
        var tasks = taskService.getTasks(user, Optional.ofNullable(status))
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        User user = getCurrentUser();
        return taskService.getTaskByIdAndUser(id, user)
                .map(task -> ResponseEntity.ok(toResponse(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest updatedRequest) {
        User user = getCurrentUser();
        return taskService.getTaskByIdAndUser(id, user)
                .map(task -> {
                    task.setTitle(updatedRequest.getTitle());
                    task.setDescription(updatedRequest.getDescription());
                    task.setStatus(updatedRequest.getStatus());
                    return ResponseEntity.ok(toResponse(taskService.updateTask(task)));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        User user = getCurrentUser();
        return taskService.getTaskByIdAndUser(id, user)
                .map(task -> {
                    taskService.deleteTask(task);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

}
