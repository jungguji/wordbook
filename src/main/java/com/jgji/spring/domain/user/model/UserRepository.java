package com.jgji.spring.domain.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    @Query("SELECT user FROM User user WHERE username =:username")
    @Transactional(readOnly = true)
    User findByUserName(@Param("username") String username);
}
