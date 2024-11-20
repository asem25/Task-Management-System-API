package com.semavin.API.repositories;

import com.semavin.API.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {
    Optional<TaskStatus> findByNameIgnoreCase(String name);
}
