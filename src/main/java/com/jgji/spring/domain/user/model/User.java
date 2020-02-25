package com.jgji.spring.domain.user.model;

import org.apache.ibatis.type.Alias;

@Alias("user")
public class User {
    
    private String id;
    private String username;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "username >> " + this.getUsername() + " password >> " + this.getPassword();
    }
}
