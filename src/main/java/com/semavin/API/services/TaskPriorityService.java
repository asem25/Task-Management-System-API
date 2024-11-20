package com.semavin.API.services;

import com.semavin.API.models.TaskPriority;
import com.semavin.API.repositories.TaskPriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskPriorityService {
    private TaskPriorityRepository taskPriorityRepository;
    @Autowired
    public TaskPriorityService(TaskPriorityRepository taskPriorityRepository) {
        this.taskPriorityRepository = taskPriorityRepository;
    }
    public TaskPriority getObjectPriorityForName(String name){
        return taskPriorityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new IllegalArgumentException("Not found priority"));
    }
}
