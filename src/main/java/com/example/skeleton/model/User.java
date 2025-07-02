package com.example.skeleton.model;

public class User {
    private Long id;
    
    public User() {
    }
    
    public User(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               '}';
    }
}