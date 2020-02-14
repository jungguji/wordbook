package com.jgji.spring.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jgji.spring.domain.user.service.UserService;

@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService; 
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication; 

        User user = (User) userService.loadUserByUsername(authToken.getName());
        if (user == null) {
          throw new UsernameNotFoundException(authToken.getName());
        }

        if (!passwordEncoder.matches((String) authToken.getCredentials(), user.getPassword())) {
          throw new BadCredentialsException("not matching username or password");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
