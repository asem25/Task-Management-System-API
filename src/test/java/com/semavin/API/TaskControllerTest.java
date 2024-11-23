package com.semavin.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semavin.API.controllers.TaskController;
import com.semavin.API.dtos.TaskDTO;
import com.semavin.API.repositories.TaskRepository;
import com.semavin.API.security.jwt.JwtAuthenticationFilter;
import com.semavin.API.security.jwt.JwtTokenProvider;
import com.semavin.API.security.service.CustomUserDetailsService;
import com.semavin.API.services.TaskPriorityService;
import com.semavin.API.services.TaskService;
import com.semavin.API.services.TaskStatusService;
import com.semavin.API.services.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskPriorityService taskPriorityService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskStatusService taskStatusService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;



    @Test
    public void testCreateTask_ShouldReturnOk() throws Exception {
        TaskDTO task = TaskDTO.builder()
                .title("Test Title")
                .priority("HIGH")
                .assigneeId(1L)
                .status("PENDING")
                .authorId(1L)
                .description("test description")
                .build();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task add with title: Test Title"));
    }

    @Test
    public void testCreateTask_WithInvalidTaskDTO_ShouldReturnBadRequest() throws Exception {

        TaskDTO task = TaskDTO.builder()
                .title("Test Title")
                .build();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().is(400));
    }

    @Test
    public void testDeleteTask_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with id - '1' deleted"));
    }
}

