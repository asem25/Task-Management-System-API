package com.semavin.API.services;

import com.semavin.API.dtos.CommentDTO;
import com.semavin.API.models.Comment;
import com.semavin.API.models.Task;
import com.semavin.API.repositories.CommentRepository;
import com.semavin.API.utils.exceptions.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
@Service
public class CommentService {
    private CommentRepository commentRepository;
    private TaskService taskService;
    private UserService userService;
    @Autowired
    public CommentService(CommentRepository commentRepository, TaskService taskService, UserService userService) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.userService = userService;
    }
    public List<CommentDTO> findAllByTaskId(Long id){
        return commentRepository.findAllByTaskId(id).stream()
                .map(this::convertToDTO)
                .toList();
    }
    public void addCommentsToTask(Long id, CommentDTO commentDTO, String currEmail){
        Task task = taskService.findById(id);
        commentRepository.save(convertToComment(commentDTO, task, currEmail));
    }
    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }
    public Comment findById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

    }
    private Comment convertToComment(CommentDTO commentDTO, Task task, String corrEmail){
        return Comment.builder()
                .content(commentDTO.getContent())
                .task(task)
                .author(userService.findUserByEmail(corrEmail))
                .createdAt(LocalDateTime.now())
                .build();
    }
    private CommentDTO convertToDTO(Comment comment){
        return CommentDTO.builder()
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorEmail(comment.getAuthor().getEmail())
                .build();
    }
}
