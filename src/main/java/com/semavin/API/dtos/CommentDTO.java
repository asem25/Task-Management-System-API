package com.semavin.API.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data@Builder
public class CommentDTO {
    private String content;
    private String authorEmail;
    private LocalDateTime createdAt;
}
