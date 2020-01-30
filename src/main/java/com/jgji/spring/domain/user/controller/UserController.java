package com.jgji.spring.domain.user.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.service.UserService;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    
    @GetMapping("/user/create")
    public String initCreationForm(Map<String, Object> model) {
        User user = new User();
        model.put("user", user);
        
        return "thymeleaf/createUserForm";
    }
    
    @PostMapping("/user/create")
    public String processCreationForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "thymeleaf/createUserForm";
        }
        
        this.userService.save(user);
        return "redirect:/";
    }
}
