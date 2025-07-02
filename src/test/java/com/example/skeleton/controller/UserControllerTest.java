package com.example.skeleton.controller;

import com.example.skeleton.model.User;
import com.example.skeleton.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.example.skeleton.config.SecurityConfig;
import com.example.skeleton.config.JwtUtil;
import com.example.skeleton.config.JwtAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtUtil.class, JwtAuthenticationFilter.class})
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUser_ReturnsRandomUserId() throws Exception {
        User mockUser = new User(12345L, "random_user", "Random User", "random@example.com");
        when(userService.generateRandomUser()).thenReturn(mockUser);
        
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(12345))
                .andExpect(jsonPath("$.username").value("random_user"))
                .andExpect(jsonPath("$.name").value("Random User"))
                .andExpect(jsonPath("$.email").value("random@example.com"));
    }

    @Test
    public void testGetUser_AccessibleWithoutAuthentication() throws Exception {
        User mockUser = new User(67890L, "random_user", "Random User", "random@example.com");
        when(userService.generateRandomUser()).thenReturn(mockUser);
        
        // This test verifies that the endpoint is accessible without authentication
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUser_WithAuthentication_Success() throws Exception {
        User inputUser = new User("john_doe", "John Doe", "john@example.com");
        User createdUser = new User(1L, "john_doe", "John Doe", "john@example.com");
        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testCreateUser_WithoutAuthentication_Unauthorized() throws Exception {
        User inputUser = new User("john_doe", "John Doe", "john@example.com");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateUser_WithInvalidData_BadRequest() throws Exception {
        User invalidUser = new User("", "John Doe", "john@example.com");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUser_WithInvalidEmail_BadRequest() throws Exception {
        User invalidUser = new User("john_doe", "John Doe", "invalid-email");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsers_WithAuthentication_Success() throws Exception {
        List<User> users = Arrays.asList(
            new User(1L, "john_doe", "John Doe", "john@example.com"),
            new User(2L, "jane_smith", "Jane Smith", "jane@example.com")
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[1].username").value("jane_smith"));
    }

    @Test
    public void testGetAllUsers_WithoutAuthentication_Unauthorized() throws Exception {
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isUnauthorized());
    }
}