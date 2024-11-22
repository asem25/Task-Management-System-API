package com.semavin.API.repositories;

import com.semavin.API.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {
    Optional<TaskStatus> findByNameIgnoreCase(String name);
}
