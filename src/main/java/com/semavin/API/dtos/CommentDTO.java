package com.semavin.API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data@Builder
@Schema(description = "DTO для комментариев")
public class CommentDTO {
    @Schema(description = "Комментарий")
    @Size(min = 5, max = 500, message = "Size field 'content' must be 5<=size<=500")
    @NotBlank
    private String content;
    @Schema(description = "Почта автора")
    @Email(message = "email must be corrent")
    @NotBlank
    private String authorEmail;
    @Schema(description = "Время создания")
    private LocalDateTime createdAt;
}
