package com.example.skeleton.controller;

import com.example.skeleton.model.User;
import com.example.skeleton.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.example.skeleton.config.SecurityConfig;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        User mockUser = new User(12345L);
        when(userService.generateRandomUser()).thenReturn(mockUser);
        
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(12345));
    }

    @Test
    public void testGetUser_AccessibleWithoutAuthentication() throws Exception {
        User mockUser = new User(67890L);
        when(userService.generateRandomUser()).thenReturn(mockUser);
        
        // This test verifies that the endpoint is accessible without authentication
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }
}