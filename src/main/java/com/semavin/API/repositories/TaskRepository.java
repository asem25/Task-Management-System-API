package com.semavin.API.repositories;

import com.semavin.API.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findAllByAuthorId(Long id);
    List<Task> findAllByAssigneeId(Long id);
    @Query("SELECT t FROM Task t " +
            "JOIN t.taskStatus ts " +
            "JOIN t.taskPriority tp " +
            "WHERE (:status IS NULL OR ts.name = :status) " +
            "AND (:priority IS NULL OR tp.name = :priority) " +
            "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)")
    Page<Task> findWithFilters(Long assigneeId, String status, String priority, Pageable pageable);
}
