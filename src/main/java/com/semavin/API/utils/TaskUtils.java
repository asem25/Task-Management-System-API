package com.semavin.API.utils;

import com.semavin.API.dtos.TaskResponseDTO;
import com.semavin.API.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskUtils {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskUtils(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponseDTO> findTasksByAuthor(Long authorId) {
        return taskRepository.findAllByAuthorId(authorId).stream()
                .map(task -> TaskResponseDTO.builder()
                        .title(task.getTitle())
                        .build())
                .toList();
    }

    public List<TaskResponseDTO> findTasksByAssignee(Long assigneeId) {
        return taskRepository.findAllByAssigneeId(assigneeId).stream()
                .map(task -> TaskResponseDTO.builder()
                        .title(task.getTitle())
                        .build())
                .toList();
    }
}
