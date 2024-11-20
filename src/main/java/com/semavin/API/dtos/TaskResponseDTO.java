package com.semavin.API.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponseDTO {
    private String title;
}
