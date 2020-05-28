package com.jgji.spring.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.model.UserDTO.CreateUser;
import com.jgji.spring.domain.user.model.UserDTO.UserProfile;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.util.Utils;
import com.jgji.spring.domain.word.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private WordService wordService;
    
    @GetMapping("/login")
    public String initLoginForm(Model model) {
        return "thymeleaf/user/viewLoginForm";
    }
    
    @GetMapping("/user/create")
    public String initCreationForm(Map<String, Object> model) {
        CreateUser user = new CreateUser();
        model.put("createUser", user);
        
        return "thymeleaf/user/createUserForm";
    }
    
    @PostMapping("/user/create")
    public String processCreationForm(@Valid CreateUser createUser, BindingResult result) {
        if (userService.isExistName(createUser.getUsername())) {
            result.rejectValue("username", "username", "이미 존재하는 아이디 입니다.");
        }
        
        if (result.hasErrors()) {
            return "thymeleaf/user/createUserForm";
        }
        
        User user = new User();
        user.setUsername(createUser.getUsername());
        user.setPassword(createUser.getPassword());
        
        this.userService.save(user);
        
        return "redirect:/";
    }
    
    @GetMapping("/user/profile")
    public ModelAndView showUserProfileForm() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("thymeleaf/user/viewUserProfileForm");
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        
        mav.addObject("user", user);
        
        ObjectMapper objMapper = Utils.getObjectMapperConfig();
        String jsonText = objMapper.writeValueAsString(wordService.findAllByUserId());
        mav.addObject("data", jsonText);
        
        String userId = user.getId();
        String graphData = objMapper.writeValueAsString(wordService.getFrequentFailWord(userId));
        mav.addObject("graphData", graphData);
        
        UserProfile userProfile = new UserProfile();
        List<Map<String, Object>> wrongWordList = userService.getMostWrongWord(userId);
        
        userProfile.setUserName(user.getUsername());
        
        for (Map<String, Object> map : wrongWordList) {
            userProfile.setMostWrongWord((String) map.get("mostWrongWord"));
            userProfile.setMostWrongCount((BigInteger) map.get("mostWrongCount"));
        }
        
        mav.addObject("userProfile", userProfile);
        
        return mav;
    }
    
    @GetMapping("/reset/password")
    public String initResetPasswordForm() {
        return "thymeleaf/user/viewForgotPasswordForm";
    }

    @PostMapping(value="/reset/password/check", produces = "application/json")
    @ResponseBody
    public String checkUserId(@RequestBody String userName) throws JsonProcessingException {
        String msg = "";
        String replaceUserName = userName.replace("\"", "");
        if (!userService.isExistName(replaceUserName)) {
            
            msg = "존재하지 않는 아이디 입니다.";
        }
        
        return Utils.returnJsonMsg(msg);
    }
    
    @PostMapping(value="/reset/password", produces = "application/json")
    @ResponseBody
    public String processResetPasswordForm(@RequestBody User user) throws JsonProcessingException {
        String tempPassword = userService.setTempPassWord(user);
        String msg = "임시 비밀번호 : " + tempPassword;
        
        return Utils.returnJsonMsg(msg);
    }
    
    @PostMapping(value="/change/password", produces = "application/json")
    @ResponseBody
    public String processChangePassword(@RequestBody UserProfile changPassword) throws JsonProcessingException {
        String msg = userService.changePassword(changPassword);
        
        return Utils.returnJsonMsg(msg);
    }
}
