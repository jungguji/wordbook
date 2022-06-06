package com.jgji.spring.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.user.dto.UserRequest;
import com.jgji.spring.domain.user.dto.UserResponse;
import com.jgji.spring.domain.user.service.UserSaveService;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.service.WordFindService;
import com.jgji.spring.domain.word.service.WordSaveService;
import com.jgji.spring.global.annotation.CurrentUser;
import com.jgji.spring.global.util.PropertiesUtil;
import com.jgji.spring.global.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final UserSaveService userSaveService;
    private final WordSaveService wordSaveService;
    private final WordFindService wordFindService;

    @GetMapping("/login")
    public String initLoginForm() {
        return "thymeleaf/user/viewLoginForm";
    }

    @GetMapping("/user/create")
    public String initCreationForm(Model model) {
        model.addAttribute("createUser", new UserRequest.CreateUser());

        return "thymeleaf/user/createUserForm";
    }

    @PostMapping("/user/create")
    public String processCreationForm(@Valid UserRequest.CreateUser createUser, BindingResult result) {
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

        this.userSaveService.save(user);

        return "redirect:/";
    }

    @GetMapping("/user/profile")
    public String showUserProfileForm(@CurrentUser User user, Model model) {

        UserResponse.DefaultUserInfo userInfo = UserResponse.DefaultUserInfo.of(user);

        List<UserResponse.MyWord> myWords = getMyWords(user);

        int userId = user.getId();
        List<UserResponse.Graph> graphs = getGraphs(userId);

        UserResponse.MostWrongWord mostWrongWord = getMostWrongWord(userId);

        UserResponse.Profile profile = UserResponse.Profile.builder()
                .user(userInfo)
                .word(myWords)
                .graph(graphs)
                .mostWrongWord(mostWrongWord)
                .build();

        model.addAttribute("profile", profile);

        return "thymeleaf/user/viewUserProfileForm";
    }

    private List<UserResponse.Graph> getGraphs(int userId) {
        List<Map<String, Object>> graphData = wordSaveService.findFrequentFailWord(userId);
        return UserResponse.Graph.ofList(graphData);
    }

    private List<UserResponse.MyWord> getMyWords(User user) {
        List<Word> words = this.wordFindService.findAllByUserId(user.getId());
        return UserResponse.MyWord.ofList(words);
    }

    private UserResponse.MostWrongWord getMostWrongWord(int userId) {
        List<Map<String, Object>> wrongWordList = userService.getMostWrongWord(userId);

        List<String> mostWrongWords = new ArrayList<>();
        List<BigInteger> mostWrongCount = new ArrayList<>();
        for (Map<String, Object> map : wrongWordList) {
            mostWrongWords.add((String) map.get("mostWrongWord"));
            mostWrongCount.add((BigInteger) map.get("mostWrongCount"));
        }

        return UserResponse.MostWrongWord.builder()
                .word(mostWrongWords)
                .count(mostWrongCount)
                .build();
    }

    @GetMapping("/reset/password")
    public String initResetPasswordForm() {
        return "thymeleaf/user/viewForgotPasswordForm";
    }

    @PostMapping(value = "/reset/password/check", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String checkUserId(@RequestBody String userName) throws JsonProcessingException {
        String msg = "";
        String replaceUserName = userName.replace("\"", "");
        if (!userService.isExistName(replaceUserName)) {
            msg = PropertiesUtil.getMessage("message.user.not.exist.id");
        }

        return Utils.returnJsonMsg(msg);
    }

    @PostMapping(value = "/reset/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String processResetPasswordForm(@RequestBody User user) throws JsonProcessingException {
        String tempPassword = this.userSaveService.changeRandomPassword(user);
        String msg = PropertiesUtil.getMessage("message.user.temp.password") + tempPassword;

        return Utils.returnJsonMsg(msg);
    }

    @PutMapping(value = "/change/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void processChangePassword(@CurrentUser User user
            , @RequestBody @Valid UserRequest.ChangePassword changPassword) {

        changPassword.validation(user);

        this.userSaveService.changePassword(user, changPassword);
    }
}
