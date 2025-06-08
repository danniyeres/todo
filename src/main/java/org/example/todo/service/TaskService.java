package org.example.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo.model.Status;
import org.example.todo.model.Task;
import org.example.todo.model.User;
import org.example.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        log.info("Create task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    public List<Task> getTasks(User user, Optional<Status> status) {
        return status.map(s -> taskRepository.findByUserAndStatus(user, s))
                .orElseGet(() -> taskRepository.findByUser(user));
    }


    public Optional<Task> getTaskByIdAndUser(Long id, User user) {
        return taskRepository.findById(id)
                .filter(task -> task.getUser().getId().equals(user.getId()));
    }

    public Task updateTask(Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("Task ID cannot be null for update");
        }
        log.info("Update task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    public void deleteTask(Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("Task ID cannot be null for delete");
        }
        log.info("Delete task: {}", task.getTitle());
        taskRepository.delete(task);
    }
}
