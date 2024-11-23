package com.semavin.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semavin.API.controllers.CommentController;
import com.semavin.API.dtos.CommentDTO;
import com.semavin.API.models.Comment;
import com.semavin.API.models.User;
import com.semavin.API.repositories.TaskRepository;
import com.semavin.API.security.jwt.JwtAuthenticationFilter;
import com.semavin.API.security.jwt.JwtTokenProvider;
import com.semavin.API.security.service.CustomUserDetailsService;
import com.semavin.API.services.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskPriorityService taskPriorityService;

    @MockBean
    private UserService userService;



    @MockBean
    private TaskStatusService taskStatusService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        // Мокируем аутентификацию
        authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Мокируем имя пользователя и его роль
        doReturn("user@test.com").when(authentication).getName();
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))).when(authentication).getAuthorities();
    }

    @Test
    public void testGetCommentsForTask_ShouldReturnOk() throws Exception {
        // Создаём комментарий с помощью Builder
        CommentDTO commentDTO = CommentDTO.builder()
                .authorEmail("user@test.com")
                .content("This is a test comment.")
                .build();

        // Мокируем метод service
        doReturn(Collections.singletonList(commentDTO)).when(commentService).findAllByTaskId(1L);

        mockMvc.perform(get("/tasks/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorEmail").value("user@test.com"))
                .andExpect(jsonPath("$[0].content").value("This is a test comment."));
    }

    @Test
    public void testAddCommentToTask_ShouldReturnOk() throws Exception {
        // Создаём DTO с помощью Builder
        CommentDTO commentDTO = CommentDTO.builder()
                .content("New comment")
                .authorEmail("user@test.com")
                .build();

        // Выполняем запрос для добавления комментария
        mockMvc.perform(post("/tasks/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment for task '1' add"));
    }

    @Test
    public void testAddCommentToTask_ShouldReturnBadRequest_WhenInvalid() throws Exception {
        // Создаём пустой DTO
        CommentDTO commentDTO = CommentDTO.builder().build();

        mockMvc.perform(post("/tasks/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isBadRequest()); // Ожидаем ошибку из-за пустых полей
    }

    @Test
    public void testDeleteComment_ShouldReturnOk_WhenUserIsAuthor() throws Exception {
        // Мокируем комментарий
        User user = User.builder().id(1L).email("user@test.com").createdAt(LocalDateTime.now()).build();
        Comment comment = Comment.builder().author(user).build();
        doReturn(comment).when(commentService).findById(1L);

        // Выполняем запрос на удаление комментария
        mockMvc.perform(delete("/comments/1")
                        .header("Authorization", "Bearer mockJwtToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete comment with id '1'"));
    }

    @Test
    public void testDeleteComment_ShouldReturnForbidden_WhenUserIsNotAuthor() throws Exception {
        // Мокируем комментарий с другим автором
        User user = User.builder().id(1L).email("anotheruser@test.com").createdAt(LocalDateTime.now()).build();
        Comment comment = Comment.builder().author(user).build();
        doReturn(comment).when(commentService).findById(1L);

        mockMvc.perform(delete("/comments/1")
                        .header("Authorization", "Bearer mockJwtToken"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("No permissions"));
    }
}
