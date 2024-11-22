package com.semavin.API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO для перемещения задач в виде ответов")
public class TaskResponseDTO {
    @Schema(description = "Название задачи")
    @Size(min = 2, max = 255, message = "Title size in 2 <= title <= 255")
    private String title;
}
