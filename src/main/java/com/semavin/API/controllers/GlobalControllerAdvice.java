package com.semavin.API.controllers;

import com.semavin.API.utils.exceptions.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException userNotFoundException){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(userNotFoundException.getMessage());
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handleRoleNotFoundException(RoleNotFoundException roleNotFoundException){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(roleNotFoundException.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException taskNotFoundException){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(taskNotFoundException.getMessage());
    }
    @ExceptionHandler(TaskPriorityNotFoundException.class)
    public ResponseEntity<String> handleTaskPriorityNotFoundException(TaskPriorityNotFoundException taskPriorityNotFoundException){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(taskPriorityNotFoundException.getMessage());
    }
    @ExceptionHandler(TaskStatusNotFoundException.class)
    public ResponseEntity<String> handleTaskStatusNotFoundException(TaskNotFoundException taskNotFoundException){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(taskNotFoundException.getMessage());
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException commentNotFoundException){
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(commentNotFoundException.getMessage());
    }
    @ExceptionHandler(FieldErrorException.class)
    public ResponseEntity<String> handleFieldErrorException(FieldErrorException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException){
        return ResponseEntity.badRequest().body(userAlreadyExistsException.getMessage());
    }
}
