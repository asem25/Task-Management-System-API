package com.semavin.API.repositories;

import com.semavin.API.models.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long> {
    Optional<TaskPriority> findByNameIgnoreCase(String name);
}