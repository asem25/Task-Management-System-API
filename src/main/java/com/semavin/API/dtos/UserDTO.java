package com.semavin.API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO для пользователя")
public class UserDTO {
    @Schema(description = "Почта")
    @Email(message = "Email must be correct")
    @NotBlank
    private String email;
    @Schema(description = "Пароль")
    @Size(min = 2, max = 255, message = "Password max size 255, min size 2")
    @NotBlank
    private String password;
    @Schema(description = "Название роли")
    private String role;
}
