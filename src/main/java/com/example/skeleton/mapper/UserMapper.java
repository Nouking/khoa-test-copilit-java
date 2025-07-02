package com.example.skeleton.mapper;

import com.example.skeleton.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT 1 as id")
    User findSampleUser();
    
    @Select("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES")
    int getTableCount();
}