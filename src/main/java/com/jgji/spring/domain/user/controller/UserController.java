package com.jgji.spring.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.user.domain.UserDTO.CreateUser;
import com.jgji.spring.domain.user.domain.UserDTO.UserProfile;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.service.WordFindService;
import com.jgji.spring.domain.word.service.WordSaveService;
import com.jgji.spring.global.annotation.CurrentUser;
import com.jgji.spring.global.util.PropertiesUtil;
import com.jgji.spring.global.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final WordSaveService wordSaveService;
    private final WordFindService wordFindService;

    @GetMapping("/login")
    public String initLoginForm() {
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
            result.rejectValue("username", "username", PropertiesUtil.getMessage("message.user.exist.name"));
        }

        if (result.hasErrors()) {
            return "thymeleaf/user/createUserForm";
        }

        User user = User.builder()
                .username(createUser.getUsername())
                .password(createUser.getPassword())
                .build();

        this.userService.save(user);

        return "redirect:/";
    }

    @GetMapping("/user/profile")
    public ModelAndView showUserProfileForm(@CurrentUser User user) throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("thymeleaf/user/viewUserProfileForm");

        mav.addObject("user", user);

        ObjectMapper objMapper = Utils.getObjectMapperConfig();
        String jsonText = objMapper.writeValueAsString(this.wordFindService.findAllByUserId(user.getId()));
        mav.addObject("data", jsonText);

        int userId = user.getId();
        String graphData = objMapper.writeValueAsString(wordSaveService.getFrequentFailWord(userId));
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

    @PostMapping(value = "/reset/password/check", produces = "application/json")
    @ResponseBody
    public String checkUserId(@RequestBody String userName) throws JsonProcessingException {
        String msg = "";
        String replaceUserName = userName.replace("\"", "");
        if (!userService.isExistName(replaceUserName)) {
            msg = PropertiesUtil.getMessage("message.user.not.exist.id");
        }

        return Utils.returnJsonMsg(msg);
    }

    @PostMapping(value = "/reset/password", produces = "application/json")
    @ResponseBody
    public String processResetPasswordForm(@RequestBody User user) throws JsonProcessingException {
        String tempPassword = userService.setTempPassWord(user);
        String msg = PropertiesUtil.getMessage("message.user.temp.password") + tempPassword;

        return Utils.returnJsonMsg(msg);
    }

    @PostMapping(value = "/change/password", produces = "application/json")
    @ResponseBody
    public String processChangePassword(@CurrentUser User user
            , @RequestBody UserProfile changPassword) throws JsonProcessingException {
        String msg = userService.changePassword(user, changPassword);

        return Utils.returnJsonMsg(msg);
    }
}
