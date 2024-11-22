package com.semavin.API.services;

import com.semavin.API.models.TaskStatus;
import com.semavin.API.repositories.TaskStatusRepository;
import com.semavin.API.utils.TaskNotFoundException;
import com.semavin.API.utils.TaskStatusNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskStatusService {
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }
    public TaskStatus getObjectTaskStatusFromName(String name){
        return taskStatusRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new TaskStatusNotFoundException("Not found status"));
    }
}
