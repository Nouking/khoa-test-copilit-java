package com.example.skeleton.mapper;

import com.example.skeleton.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT 1 as id")
    User findSampleUser();
    
    @Select("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES")
    int getTableCount();
    
    @Select("SELECT * FROM users")
    List<User> findAllUsers();
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findUserById(Long id);
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findUserByUsername(String username);
    
    @Insert("INSERT INTO users (username, name, email) VALUES (#{username}, #{name}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);
    
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(String username);
    
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(String email);
}