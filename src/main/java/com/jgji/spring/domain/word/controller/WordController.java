package com.jgji.spring.domain.word.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.word.domain.Row;
import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.domain.WordDTO;
import com.jgji.spring.domain.word.domain.WordDTO.AddWord;
import com.jgji.spring.domain.word.service.WordFindService;
import com.jgji.spring.domain.word.service.WordSaveService;
import com.jgji.spring.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String getToDayWordList(@CurrentUser User user, Model model) throws JsonProcessingException {
        List<WordDTO.ResponseWord> list = this.wordFindService.findToDayTestWordList(user.getId());

        String jsonText = objectMapper.writeValueAsString(list);

        model.addAttribute("wordList", list);

        return "thymeleaf/word/viewWordTestForm";
    }

    @PostMapping(path="/word/answers")
    @ResponseBody
    public List<String> updateNextDateAndInsert(@CurrentUser User user
            , @RequestParam("pass") int[] passIds, @RequestParam("fail") int[] failIds) {
        this.wordSaveService.updatePassWord(user, passIds);
        List<String> failList = this.wordSaveService.insertFailWord(user, failIds);
        return failList;
    }

    @PostMapping(path="/word/answers/random", produces = "application/json")
    @ResponseBody
    public boolean insertRandomFailWord(@CurrentUser User user
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
    public String createWordByFileUpload(@CurrentUser User user
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
    public String createWord(@CurrentUser User user
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