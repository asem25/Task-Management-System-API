package com.semavin.API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO для обновления задач")
public class TaskUpdateDTO {
    @Size(min = 2, max = 255, message = "Title size in 2 <= title <= 255")
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String status;
    @NotBlank
    private String priority;
}
