package com.semavin.API.controllers;

import com.semavin.API.dtos.TaskDTO;
import com.semavin.API.dtos.TaskUpdateDTO;
import com.semavin.API.models.Task;
import com.semavin.API.services.TaskService;
import com.semavin.API.utils.exceptions.TaskFieldsErrorException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "API для работы с задачами")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Получить список задач", description = "Возвращает задачи с фильтрацией и пагинацией для авторизованного пользователя")
    @GetMapping
    public ResponseEntity<Page<TaskDTO>> findAllWithFilters(
            @Parameter(description = "Фильтр по статусу задачи") @RequestParam(required = false) String status,
            @Parameter(description = "Фильтр по приоритету задачи") @RequestParam(required = false) String priority,
            @Parameter(description = "Номер страницы для пагинации") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы для пагинации") @RequestParam(defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDTO> tasks = taskService.findByFilters(status, priority, currentUserEmail, pageable);

        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Создать задачу", description = "Позволяет администратору создавать новую задачу")
    @Parameter(
            name = "task",
            description = "Данные задачи",
            required = true,
            schema = @Schema(implementation = TaskDTO.class)
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskDTO task,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new TaskFieldsErrorException(bindingResult.getFieldErrors());
        }
        taskService.save(task);
        return ResponseEntity.ok("Task add with title: " + task.getTitle());
    }

    @Operation(summary = "Обновить задачу", description = "Позволяет обновлять задачу, если пользователь является администратором или исполнителем")
    @Parameter(
            name = "TaskUpdate",
            description = "Данные для обновления задачи",
            required = true,
            schema = @Schema(implementation = TaskUpdateDTO.class)
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(
            @Parameter(description = "ID задачи для обновления") @PathVariable Long id,
            @RequestBody @Valid TaskUpdateDTO taskUpdateDTO,
            BindingResult bindingResult
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currEmail = authentication.getName();

        Task task = taskService.findById(id);

        boolean isAssignee = task.getAssignee().getEmail().equals(currEmail);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAssignee && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not permissions");
        }
        if (bindingResult.hasErrors()){
            throw new TaskFieldsErrorException(bindingResult.getFieldErrors());
        }
        taskService.updateTask(id, taskUpdateDTO);
        return ResponseEntity.ok("Task updated successfully");
    }

    @Operation(summary = "Удалить задачу", description = "Позволяет администратору удалять задачи")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@Parameter(description = "id задачи") @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task with id - '" + id + "' deleted");
    }
}
