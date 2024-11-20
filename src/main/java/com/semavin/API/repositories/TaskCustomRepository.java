package com.semavin.API.repositories;

import com.semavin.API.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskCustomRepository {
    Page<Task> findWithFilters(String status, String priority, Long authorId, Long assigneeId, Pageable pageable);
}
