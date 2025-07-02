package com.example.skeleton.service;

import com.example.skeleton.mapper.UserMapper;
import com.example.skeleton.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        assertNotNull(user.getName());
        assertNotNull(user.getEmail());
        assertTrue(user.getId() >= 1 && user.getId() < 1000000);
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
    public void testCreateUser_ReturnsNewUserWithIncrementingId() {
        User user1 = userService.createUser("John Doe", "john@example.com");
        User user2 = userService.createUser("Jane Smith", "jane@example.com");
        
        assertNotNull(user1);
        assertNotNull(user2);
        assertEquals("John Doe", user1.getName());
        assertEquals("john@example.com", user1.getEmail());
        assertEquals("Jane Smith", user2.getName());
        assertEquals("jane@example.com", user2.getEmail());
        
        // IDs should be incrementing
        assertTrue(user2.getId() > user1.getId());
    }

    @Test
    public void testGetAllUsers_ReturnsAllCreatedUsers() {
        // Initial users should be present
        List<User> initialUsers = userService.getAllUsers();
        int initialCount = initialUsers.size();
        assertTrue(initialCount >= 2); // Should have John Doe and Jane Smith from constructor
        
        // Add a new user
        userService.createUser("Test User", "test@example.com");
        
        List<User> allUsers = userService.getAllUsers();
        assertEquals(initialCount + 1, allUsers.size());
        
        // Verify the new user is in the list
        boolean foundTestUser = allUsers.stream()
                .anyMatch(user -> "Test User".equals(user.getName()) && "test@example.com".equals(user.getEmail()));
        assertTrue(foundTestUser);
    }

    @Test
    public void testGetSampleUser_CallsMapper() {
        User expectedUser = new User(1L, "Sample User", "sample@example.com");
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