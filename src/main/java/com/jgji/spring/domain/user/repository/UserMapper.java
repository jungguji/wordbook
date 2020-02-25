package com.jgji.spring.domain.user.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jgji.spring.domain.user.model.User;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUserName(@Param("username") String username);
    
    @Update("UPDATE users SET password = #{password} WHERE username = #{username}")
    int updatePassword(@Param("username") String username, @Param("password") String password);
    
    @Insert("INSERT INTO users(username, password) VALUES(#{username}, #{password})")
    int create(@Param("username") String username, @Param("password") String password);
}
