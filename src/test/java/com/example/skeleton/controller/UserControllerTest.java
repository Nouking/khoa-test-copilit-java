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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
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
        User mockUser = new User(12345L, "Test User", "test@example.com");
        when(userService.generateRandomUser()).thenReturn(mockUser);
        
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(12345))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testGetUser_AccessibleWithoutAuthentication() throws Exception {
        User mockUser = new User(67890L, "Public User", "public@example.com");
        when(userService.generateRandomUser()).thenReturn(mockUser);
        
        // This test verifies that the endpoint is accessible without authentication
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUser_WithAuthentication_Success() throws Exception {
        User createdUser = new User(1L, "John Doe", "john@example.com");
        when(userService.createUser("John Doe", "john@example.com")).thenReturn(createdUser);

        Map<String, String> userRequest = new HashMap<>();
        userRequest.put("name", "John Doe");
        userRequest.put("email", "john@example.com");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testCreateUser_WithoutAuthentication_Unauthorized() throws Exception {
        Map<String, String> userRequest = new HashMap<>();
        userRequest.put("name", "John Doe");
        userRequest.put("email", "john@example.com");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateUser_WithBasicAuth_Success() throws Exception {
        User createdUser = new User(2L, "Jane Doe", "jane@example.com");
        when(userService.createUser("Jane Doe", "jane@example.com")).thenReturn(createdUser);

        Map<String, String> userRequest = new HashMap<>();
        userRequest.put("name", "Jane Doe");
        userRequest.put("email", "jane@example.com");

        mockMvc.perform(post("/user")
                .with(httpBasic("admin", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testCreateUser_WithInvalidData_BadRequest() throws Exception {
        Map<String, String> userRequest = new HashMap<>();
        userRequest.put("name", "");
        userRequest.put("email", "john@example.com");

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());
    }
}