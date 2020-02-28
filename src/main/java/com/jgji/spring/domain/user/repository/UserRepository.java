package com.jgji.spring.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.jgji.spring.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    @Query("SELECT user FROM User user WHERE username =:username")
    @Transactional(readOnly = true)
    User findByUserName(@Param("username") String username);
    
    @Query(value =
           " SELECT                                     " + 
           "     w.word, COUNT(1)                       " + 
           " FROM                                       " + 
           "     Word w                                 " + 
           " WHERE                                      " +
           "    w.users_id =:userId                     " + 
           " GROUP BY                                   " + 
           "     w.word                                 " + 
           " HAVING                                     " + 
           "     COUNT(1) = (                           " + 
           "             SELECT                         " + 
           "                 MAX(cnt)                   " + 
           "             FROM (                         " + 
           "                     SELECT                 " + 
           "                         COUNT(1) AS cnt    " + 
           "                     FROM                   " + 
           "                         Word w             " + 
           "                     WHERE                  " + 
           "                         w.users_id =:userId" + 
           "                     GROUP BY               " + 
           "                         w.word             " + 
           "                  ) AS maxfrom              " + 
           "             )                              "
           , nativeQuery = true)
    @Transactional(readOnly = true)
    List<Object[]> findMostWrongWord(@Param("userId") String userId);
}
