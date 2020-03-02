package com.jgji.spring.domain.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        System.out.println("@@@@@@@@@@@@@@@@@@");
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("password", request.getParameter("password"));
        
        String errorMsg = "";
        if ((exception instanceof BadCredentialsException) || (exception instanceof InternalAuthenticationServiceException)) {
            errorMsg = "아이디 혹은 비밀번호가 올바르지 않습니다.";
        }
        
        request.setAttribute("errorMsg", errorMsg);
        
        request.getRequestDispatcher("/login").forward(request, response);
    }

}
