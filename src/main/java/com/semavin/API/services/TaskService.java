package com.semavin.API.services;

import com.semavin.API.dtos.TaskDTO;
import com.semavin.API.dtos.TaskUpdateDTO;
import com.semavin.API.models.Task;
import com.semavin.API.models.User;
import com.semavin.API.repositories.TaskRepository;
import com.semavin.API.utils.TaskNotFoundException;
import com.semavin.API.utils.TaskPriorityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;
    private TaskPriorityService taskPriorityService;
    private UserService userService;
    private TaskStatusService taskStatusService;
    @Autowired
    public TaskService(TaskRepository taskRepository, TaskPriorityService taskPriorityService, UserService userService, TaskStatusService taskStatusService) {
        this.taskRepository = taskRepository;
        this.taskPriorityService = taskPriorityService;
        this.userService = userService;
        this.taskStatusService = taskStatusService;
    }


    public Page<TaskDTO> findByFilters(String status, String priority, String currentUserEmail, Pageable pageable) {
        User currentUser = userService.findUserByEmail(currentUserEmail);

        return taskRepository.findWithFilters(currentUser.getId(), status, priority,  pageable)
                .map(this::convertToDTO);
    }

    public Task findByTitle(String TaskTitle){
        return (taskRepository.findByTitleIgnoreCase(TaskTitle)
                .orElseThrow(() -> new TaskNotFoundException("Task not found exception")));
    }
    public void save(TaskDTO taskDTO){
        Task toSave = convertFromDTO(taskDTO);
        toSave.setCreatedAt(LocalDateTime.now());
        toSave.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(toSave);
    }
    public Task findById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException("Task not found"));
    }
    public void updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Not found"));

        task.setUpdatedAt(LocalDateTime.now());
        task.setTaskPriority(taskPriorityService.getObjectPriorityForName(taskUpdateDTO.getPriority()));
        task.setTaskStatus(taskStatusService.getObjectTaskStatusFromName(taskUpdateDTO.getStatus()));
        task.setDescription(taskUpdateDTO.getDescription());
        task.setTitle(taskUpdateDTO.getTitle());

        taskRepository.save(task);
    }
    public void deleteTask(Long id) {
        taskRepository.delete(this.findById(id));
    }

    private TaskDTO convertToDTO(Task task){
        return TaskDTO.builder()
                .priority(task.getTaskPriority().getName())
                .status(task.getTaskStatus().getName())
                .title(task.getTitle())
                .assigneeId(task.getAssignee().getId())
                .authorId(task.getAuthor().getId())
                .build();
    }
    private Task convertFromDTO(TaskDTO taskDTO){
        return Task.builder()
                .taskPriority(taskPriorityService.getObjectPriorityForName(taskDTO.getPriority()))
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .taskStatus(taskStatusService.getObjectTaskStatusFromName(taskDTO.getStatus()))
                .assignee(userService.findUserById(taskDTO.getAssigneeId()))
                .author(userService.findUserById(taskDTO.getAuthorId()))
                .build();

    }

}
