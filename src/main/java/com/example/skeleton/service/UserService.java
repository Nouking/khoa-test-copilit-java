package com.example.skeleton.service;

import com.example.skeleton.mapper.UserMapper;
import com.example.skeleton.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final Random random = new Random();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<User> users = new ArrayList<>(); // In-memory storage for demo

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
        // Initialize with some sample users
        users.add(new User(1L, "John Doe", "john@example.com"));
        users.add(new User(2L, "Jane Smith", "jane@example.com"));
    }

    public User generateRandomUser() {
        Long randomId = random.nextLong(1, 1000000);
        return new User(randomId, "Random User", "random@example.com");
    }
    
    public User createUser(String name, String email) {
        Long id = idGenerator.incrementAndGet();
        User newUser = new User(id, name, email);
        users.add(newUser);
        return newUser;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
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