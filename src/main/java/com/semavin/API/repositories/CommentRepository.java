package com.semavin.API.repositories;

import com.semavin.API.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Task, Long> {
}
