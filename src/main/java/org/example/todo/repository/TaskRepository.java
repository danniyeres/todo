package org.example.todo.repository;

import org.example.todo.model.Status;
import org.example.todo.model.Task;
import org.example.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserAndStatus(User user, Status status);
    List<Task> findByUser(User user);
}
