package com.example.skeleton.service;

import com.example.skeleton.mapper.UserMapper;
import com.example.skeleton.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

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
        assertNotNull(user.getUsername());
        assertNotNull(user.getName());
        assertNotNull(user.getEmail());
        assertTrue(user.getId() >= 1 && user.getId() < 1000000);
        assertEquals("random_user", user.getUsername());
        assertEquals("Random User", user.getName());
        assertEquals("random@example.com", user.getEmail());
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
    public void testCreateUser_Success() {
        User user = new User("john_doe", "John Doe", "john@example.com");
        when(userMapper.countByUsername("john_doe")).thenReturn(0);
        when(userMapper.countByEmail("john@example.com")).thenReturn(0);
        when(userMapper.insertUser(user)).thenReturn(1);
        
        User result = userService.createUser(user);
        
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        
        verify(userMapper, times(1)).countByUsername("john_doe");
        verify(userMapper, times(1)).countByEmail("john@example.com");
        verify(userMapper, times(1)).insertUser(user);
    }

    @Test
    public void testCreateUser_ThrowsException_WhenUsernameExists() {
        User user = new User("john_doe", "John Doe", "john@example.com");
        when(userMapper.countByUsername("john_doe")).thenReturn(1);
        
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        
        verify(userMapper, times(1)).countByUsername("john_doe");
        verify(userMapper, never()).countByEmail(anyString());
        verify(userMapper, never()).insertUser(any());
    }

    @Test
    public void testCreateUser_ThrowsException_WhenEmailExists() {
        User user = new User("john_doe", "John Doe", "john@example.com");
        when(userMapper.countByUsername("john_doe")).thenReturn(0);
        when(userMapper.countByEmail("john@example.com")).thenReturn(1);
        
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        
        verify(userMapper, times(1)).countByUsername("john_doe");
        verify(userMapper, times(1)).countByEmail("john@example.com");
        verify(userMapper, never()).insertUser(any());
    }

    @Test
    public void testGetAllUsers_ReturnsUsersFromDatabase() {
        List<User> expectedUsers = Arrays.asList(
            new User(1L, "john_doe", "John Doe", "john@example.com"),
            new User(2L, "jane_smith", "Jane Smith", "jane@example.com")
        );
        when(userMapper.findAllUsers()).thenReturn(expectedUsers);
        
        List<User> result = userService.getAllUsers();
        
        assertEquals(expectedUsers, result);
        verify(userMapper, times(1)).findAllUsers();
    }

    @Test
    public void testGetUserById() {
        User expectedUser = new User(1L, "john_doe", "John Doe", "john@example.com");
        when(userMapper.findUserById(1L)).thenReturn(expectedUser);
        
        User result = userService.getUserById(1L);
        
        assertEquals(expectedUser, result);
        verify(userMapper, times(1)).findUserById(1L);
    }

    @Test
    public void testGetUserByUsername() {
        User expectedUser = new User(1L, "john_doe", "John Doe", "john@example.com");
        when(userMapper.findUserByUsername("john_doe")).thenReturn(expectedUser);
        
        User result = userService.getUserByUsername("john_doe");
        
        assertEquals(expectedUser, result);
        verify(userMapper, times(1)).findUserByUsername("john_doe");
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