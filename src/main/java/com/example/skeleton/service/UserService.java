package com.example.skeleton.service;

import com.example.skeleton.mapper.UserMapper;
import com.example.skeleton.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final Random random = new Random();

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User generateRandomUser() {
        Long randomId = random.nextLong(1, 1000000);
        return new User(randomId, "random_user", "Random User", "random@example.com");
    }
    
    public User createUser(User user) throws IllegalArgumentException {
        // Check if username already exists
        if (userMapper.countByUsername(user.getUsername()) > 0) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email already exists
        if (userMapper.countByEmail(user.getEmail()) > 0) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        userMapper.insertUser(user);
        return user;
    }
    
    public List<User> getAllUsers() {
        return userMapper.findAllUsers();
    }
    
    public User getUserById(Long id) {
        return userMapper.findUserById(id);
    }
    
    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }
    
    public User getSampleUser() {
        return userMapper.findSampleUser();
    }
    
    public boolean isDatabaseConnected() {
        try {
            int count = userMapper.getTableCount();
            return count >= 0;
        } catch (Exception e) {
            return false;
        }
    }
}