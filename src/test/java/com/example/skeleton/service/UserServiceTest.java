package com.example.skeleton.service;

import com.example.skeleton.mapper.UserMapper;
import com.example.skeleton.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userMapper);
    }

    @Test
    public void testGenerateRandomUser_ReturnsUserWithRandomId() {
        User user = userService.generateRandomUser();
        
        assertNotNull(user);
        assertNotNull(user.getId());
        assertTrue(user.getId() >= 1 && user.getId() < 1000000);
    }

    @Test
    public void testGenerateRandomUser_GeneratesDifferentIds() {
        User user1 = userService.generateRandomUser();
        User user2 = userService.generateRandomUser();
        
        assertNotNull(user1.getId());
        assertNotNull(user2.getId());
        // Note: There's a small chance these could be equal, but very unlikely
    }

    @Test
    public void testGetSampleUser_CallsMapper() {
        User expectedUser = new User(1L);
        when(userMapper.findSampleUser()).thenReturn(expectedUser);
        
        User result = userService.getSampleUser();
        
        assertEquals(expectedUser, result);
        verify(userMapper, times(1)).findSampleUser();
    }

    @Test
    public void testIsDatabaseConnected_ReturnsTrue_WhenMapperSucceeds() {
        when(userMapper.getTableCount()).thenReturn(5);
        
        boolean result = userService.isDatabaseConnected();
        
        assertTrue(result);
        verify(userMapper, times(1)).getTableCount();
    }

    @Test
    public void testIsDatabaseConnected_ReturnsFalse_WhenMapperThrowsException() {
        when(userMapper.getTableCount()).thenThrow(new RuntimeException("Database error"));
        
        boolean result = userService.isDatabaseConnected();
        
        assertFalse(result);
        verify(userMapper, times(1)).getTableCount();
    }
}