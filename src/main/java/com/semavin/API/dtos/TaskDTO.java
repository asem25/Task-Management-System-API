package com.semavin.API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TaskDTO {
    @Schema(description = "Название задачи", example = "Исправить ошибку входа")
    @Size(min = 2, max = 255, message = "Title size in 2 <= title <= 255")
    private String title;

    @Schema(description = "Описание задачи", example = "Проверить и устранить ошибку при входе в систему")
    @NotBlank
    private String description;

    @Schema(description = "Статус задачи", example = "В процессе")
    @NotBlank
    private String status;

    @Schema(description = "Приоритет задачи", example = "Высокий")
    @NotBlank
    private String priority;

    @Schema(description = "ID автора задачи", example = "1")
    @NotBlank
    private Long authorId;
    @NotBlank
    @Schema(description = "ID исполнителя задачи", example = "2")
    private Long assigneeId;
}