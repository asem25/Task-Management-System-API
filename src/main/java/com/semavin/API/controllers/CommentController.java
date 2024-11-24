package com.semavin.API.controllers;

import com.semavin.API.dtos.CommentDTO;
import com.semavin.API.models.Comment;
import com.semavin.API.services.CommentService;
import com.semavin.API.utils.exceptions.CommentFieldsErrorException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@Tag(name = "Comments", description = "API для работы с комментариями")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Получить комментарии задачи", description = "Возвращает список комментариев для указанной задачи")
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsForTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.findAllByTaskId(taskId));
    }

    @Operation(summary = "Добавить комментарий к задаче", description = "Позволяет пользователю добавить комментарий к задаче")
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<?> addCommentsToTask(@PathVariable Long taskId,
                                               @Valid @RequestBody CommentDTO commentDTO,
                                               BindingResult bindingResult) throws CommentFieldsErrorException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currEmail = authentication.getName();

        if (bindingResult.hasErrors()){
            throw  new CommentFieldsErrorException(bindingResult.getFieldErrors());
        }

        commentService.addCommentsToTask(taskId, commentDTO, currEmail);
        return ResponseEntity.ok("Comment for task '" + taskId + "' add");
    }

    @Operation(summary = "Удалить комментарий", description = "Удаляет комментарий, если пользователь является автором или администратором")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currEmail = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Comment comment = commentService.findById(id);
        if (!currEmail.equals(comment.getAuthor().getEmail()) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No permissions");
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok("Delete comment with id '" + id + "'");
    }
}

