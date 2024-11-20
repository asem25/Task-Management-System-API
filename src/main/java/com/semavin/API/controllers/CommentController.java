package com.semavin.API.controllers;

import com.semavin.API.dtos.CommentDTO;
import com.semavin.API.models.Comment;
import com.semavin.API.models.Task;
import com.semavin.API.services.CommentService;
import com.semavin.API.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
public class CommentController {
    private CommentService commentService;
    private TaskService taskService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsForTask(@PathVariable Long taskId){
        return ResponseEntity.ok(commentService.findAllByTaskId(taskId));
    }
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<?> addCommentsToTask(@PathVariable Long taskId, @RequestBody CommentDTO commentDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currEmail = authentication.getName();

        commentService.addCommentsToTask(taskId, commentDTO, currEmail);
        return ResponseEntity.ok("Comment for task '" + taskId + "' add");
    }
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currEmail = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Comment comment = commentService.findById(id);
        if (!currEmail.equals(comment.getAuthor().getEmail()) && !isAdmin){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No permissions");
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok("Delete comment with id '" + id + "'");
    }
}
