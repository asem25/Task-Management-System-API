package com.semavin.API.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDTO {
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long authorId;
    private Long assigneeId;
}