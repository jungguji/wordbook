package com.jgji.spring.domain.word.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgji.spring.domain.user.domain.AccountAdapter;
import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.word.service.WordFindService;
import com.jgji.spring.domain.word.service.WordSaveService;
import com.jgji.spring.global.util.Utils;
import com.jgji.spring.domain.word.domain.Row;
import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.domain.WordDTO;
import com.jgji.spring.domain.word.domain.WordDTO.AddWord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class WordController {

    private final WordFindService wordFindService;
    private final WordSaveService wordSaveService;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public String home() {
        return "thymeleaf/index";
    }

    @GetMapping("/word/test")
    public String getToDayWordList(@AuthenticationPrincipal(expression = "account") User user, Model model) throws JsonProcessingException {
        List<WordDTO.ResponseWord> list = this.wordFindService.findToDayTestWordList(user.getId());

        String jsonText = objectMapper.writeValueAsString(list);

        model.addAttribute("wordList", list);

        return "thymeleaf/word/viewWordTestForm";
    }

    @PostMapping(path="/word/answers")
    @ResponseBody
    public List<String> updateNextDateAndInsert(@AuthenticationPrincipal User user
            , @RequestParam("pass") int[] passIds, @RequestParam("fail") int[] failIds) {
        this.wordSaveService.updatePassWord(user, passIds);
        List<String> failList = this.wordSaveService.insertFailWord(user, failIds);
        return failList;
    }

    @PostMapping(path="/word/answers/random", produces = "application/json")
    @ResponseBody
    public boolean insertRandomFailWord(@AuthenticationPrincipal User user
            , @RequestBody String[] answerIds) {
        return this.wordSaveService.insertRandomFailWord(user, answerIds);
    }

    @GetMapping(value="/word/add")
    public String getWordAdd(AddWord word, Model model) {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());

        return "thymeleaf/word/createWordForm";
    }

    @GetMapping(value="/word/add/file")
    public String getWordAddByFileUpload()  {
        return "thymeleaf/word/createWordFileUploadForm";
    }

    @RequestMapping(value="/word/add/upload", method=RequestMethod.POST, headers = "content-type=multipart/form-data")
    @ResponseBody
    public String createWordByFileUpload(@AuthenticationPrincipal User user
            , @RequestParam(value="file") MultipartFile files) throws IOException {
        MultipartFile file = files;

        return this.wordSaveService.insertWordByFileUpload(user, file);
    }

    @PostMapping(value="/word/add", params={"addRow"})
    public String addRow(final AddWord word, final BindingResult bindingResult) {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());

        return "thymeleaf/word/createWordForm";
    }

    @PostMapping(value="/word/add", params={"removeRow"})
    public String removeRow(final AddWord word, final BindingResult bindingResult) {
        if (word.getWords().size() > 1) {
            word.getWords().remove(word.getWords().size()-1);
            word.getMeanings().remove(word.getMeanings().size()-1);
        }

        return "thymeleaf/word/createWordForm";
    }

    @PostMapping(value="/word/add", params= {"save"})
    public String createWord(@AuthenticationPrincipal User user
            , @Valid AddWord word, BindingResult bindingResult) {
        BindingResult result = this.wordSaveService.getCreateWordBindingResult(word, bindingResult);

        if (result.hasErrors()) {
            return "thymeleaf/word/createWordForm";
        }

        this.wordSaveService.insertWord(user, word);
        return "thymeleaf/index";
    }

    @PostMapping(value="/word/update", produces = "application/json")
    @ResponseBody
    public boolean updateMeaning(@RequestBody Word word) {
        return this.wordSaveService.updateMeaning(word);
    }

    @DeleteMapping(value="/word/delete", produces = "application/json")
    @ResponseBody
    public String delete(@RequestBody String[] rowIds) {
        this.wordSaveService.delete(rowIds);

        return "good";
    }
}