package com.jgji.spring.domain.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.model.UserDTO.ChangePassword;
import com.jgji.spring.domain.user.model.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
    
    public User save(User user) {
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }
    
    public String getCurrentUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    
    public User getUserByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return user;
    }
    
    public String getUserIdByUserName() {
        User user = userRepository.findByUserName(getCurrentUserName());
        return user.getId();
    }
    
    public String getUserIdByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        return user.getId();
    }
    
    public boolean isExistName(String userName) {
        if (ObjectUtils.isEmpty(userRepository.findByUserName(userName))) {
            return false;
        }
        
        return true;
    }
    
    public String setTempPassWord(User user) {
        User updateUser = getUserByUserName(user.getUsername());
        String tempPassword = getTempPassword();
        
        updateUser.setPassword(tempPassword);
        save(updateUser);
        
        return tempPassword;
    }
    
    private String getTempPassword() {
        char passwordArray[] = new char[] {
                          '0','1','2','3','4','5','6','7','8','9',
                          'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                          'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                          '!','@','#','$','%','^','&','*','(',')'};

        StringBuilder ranPw = new StringBuilder();

        for (int i = 0; i < 16; i++) {
          int selectRandomPw = (int)(Math.random()*(passwordArray.length));
          ranPw.append(passwordArray[selectRandomPw]);
        }
        
      return ranPw.toString();
    }
    
    public void test(ChangePassword changePassword) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String oldPassword = changePassword.getOldPassword();
        
        User user = getUserByUserName(authToken.getName());
        
        if (!bcryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            System.out.println("AASDASDASDASD");
        }
    }
}
