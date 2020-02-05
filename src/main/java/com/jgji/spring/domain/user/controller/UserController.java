package com.jgji.spring.domain.user.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.service.WordService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private WordService wordService;
    
    @GetMapping("/user/create")
    public String initCreationForm(Map<String, Object> model) {
        User user = new User();
        model.put("user", user);
        
        return "thymeleaf/createUserForm";
    }
    
    @PostMapping("/user/create")
    public String processCreationForm(@Valid User user, BindingResult result) {
        if (userService.isExistName(user.getUsername())) {
            result.rejectValue("username", "username", "이미 존재하는 아이디 입니다.");
        }
        
        if (result.hasErrors()) {
            return "thymeleaf/createUserForm";
        }
        
        this.userService.save(user);
        return "redirect:/";
    }
    
    @GetMapping("/user/profile")
    public ModelAndView showUserProfileForm() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("thymeleaf/viewUserProfileForm");
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        System.out.println(user.getUsername());
        
        mav.addObject("user", user);
        
        ObjectMapper objMapper = getObjectMapperConfig();
        String jsonText = objMapper.writeValueAsString(wordService.findAllByUserId());
        mav.addObject("data", jsonText);
        
        return mav;
    }
    
    private ObjectMapper getObjectMapperConfig() {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        return objMapper;
    }
}
