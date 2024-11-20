package com.semavin.API.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskUpdateDTO {
    private String title;
    private String description;
    private String status;
    private String priority;
}
