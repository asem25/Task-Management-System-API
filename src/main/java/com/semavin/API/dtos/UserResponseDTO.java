package com.semavin.API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
@Schema(description = "DTO для перемещия пользователя")
public class UserResponseDTO {
    private String email;
    private String role;
    @Schema(description = "Список задач, где пользователь автор")
    private List<TaskResponseDTO> tasksWhereAuthors;
    @Schema(description = "Список задач, где пользователь исполнитель")
    private List<TaskResponseDTO> taskWhereAssignee;
}
