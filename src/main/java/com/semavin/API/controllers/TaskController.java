package com.semavin.API.controllers;

import com.semavin.API.dtos.TaskDTO;
import com.semavin.API.dtos.TaskUpdateDTO;
import com.semavin.API.models.Task;
import com.semavin.API.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/tasks")

public class TaskController {
    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> findAllWithFilters(@RequestParam(required = false) String status,
                                                            @RequestParam(required = false) String priority,
                                                            @RequestParam(required = false) Long authorId,
                                                            @RequestParam(required = false) Long assigneeId,
                                                            @RequestParam(required = false) Pageable pageable){
        return ResponseEntity.ok(taskService.findByFilters(status, priority, authorId, assigneeId, pageable));
    }
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@RequestBody TaskDTO task){
        taskService.save(task);
        return ResponseEntity.ok("Task add with title: " + task.getTitle());
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currEmail = authentication.getName();

        Task task = taskService.findById(id);

        boolean isAssignee = task.getAssignee().getEmail().equals(currEmail);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));


        if (!isAssignee && !isAdmin){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not permissions");
        }


        taskService.updateTask(id, taskUpdateDTO);

        return ResponseEntity.ok("Task updated successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task with id - '" + id + "'" + "updated");
    }

}
