package com.example.skeleton.service;

import com.example.skeleton.mapper.UserMapper;
import com.example.skeleton.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return new User(randomId);
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