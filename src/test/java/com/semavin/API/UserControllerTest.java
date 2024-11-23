package com.semavin.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semavin.API.controllers.UserController;
import com.semavin.API.dtos.UserResponseDTO;
import com.semavin.API.repositories.TaskRepository;
import com.semavin.API.security.jwt.JwtAuthenticationFilter;
import com.semavin.API.security.jwt.JwtTokenProvider;
import com.semavin.API.security.service.CustomUserDetailsService;
import com.semavin.API.services.TaskPriorityService;
import com.semavin.API.services.TaskService;
import com.semavin.API.services.TaskStatusService;
import com.semavin.API.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

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
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    public void testShowAllUsers_ShouldReturnOk() throws Exception {
        // Создаем тестовых пользователей
        UserResponseDTO user1 = UserResponseDTO.builder()
                .email("admin@test.com")
                .role("ADMIN")
                .tasksWhereAuthors(List.of())  // Пустой список задач
                .tasksWhereAssignee(List.of()) // Пустой список задач
                .build();
        UserResponseDTO user2 = UserResponseDTO.builder()
                .email("user@test.com")
                .role("USER")
                .tasksWhereAuthors(List.of())  // Пустой список задач
                .tasksWhereAssignee(List.of()) // Пустой список задач
                .build();

        List<UserResponseDTO> users = Arrays.asList(user1, user2);

        // Мокируем сервис
        when(userService.showAll()).thenReturn(users);

        // Выполнение GET-запроса и проверка ответа
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))  // Проверка количества пользователей в ответе
                .andExpect(jsonPath("$[0].email").value("admin@test.com"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))
                .andExpect(jsonPath("$[1].email").value("user@test.com"))
                .andExpect(jsonPath("$[1].role").value("USER"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    public void testShowUserById_ShouldReturnUserInfo() throws Exception {
        // Создаем тестового пользователя
        UserResponseDTO user = UserResponseDTO.builder()
                .email("admin@test.com")
                .role("ADMIN")
                .tasksWhereAuthors(List.of())  // Пустой список задач
                .tasksWhereAssignee(List.of()) // Пустой список задач
                .build();

        // Мокируем сервис
        when(userService.findUserResponseDTOById(anyLong())).thenReturn(user);

        // Выполнение GET-запроса с ID пользователя и проверка ответа
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    public void testShowUserById_WithoutAdminRole_ShouldReturnCurrentUser() throws Exception {
        // Создаем тестового пользователя
        UserResponseDTO user = UserResponseDTO.builder()
                .email("user@test.com")
                .role("USER")
                .tasksWhereAuthors(List.of())  // Пустой список задач
                .tasksWhereAssignee(List.of()) // Пустой список задач
                .build();

        // Мокируем сервис
        when(userService.findUserResponseByEmail("user@test.com")).thenReturn(user);

        // Выполнение GET-запроса с ID пользователя и проверка ответа
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}
