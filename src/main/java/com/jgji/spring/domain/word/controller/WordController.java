package com.jgji.spring.domain.word.controller;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.word.domain.Row;
import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.domain.WordDTO;
import com.jgji.spring.domain.word.domain.WordDTO.AddWord;
import com.jgji.spring.domain.word.dto.WordRequest;
import com.jgji.spring.domain.word.service.WordFindService;
import com.jgji.spring.domain.word.service.WordSaveService;
import com.jgji.spring.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class WordController {

    private final WordFindService wordFindService;
    private final WordSaveService wordSaveService;

    @GetMapping("/")
    public String home() {
        return "thymeleaf/index";
    }

    @GetMapping("/word/test")
    public String getToDayWordList(@CurrentUser User user, Model model) {
        List<WordDTO.ResponseWord> list = this.wordFindService.findToDayTestWordList(user.getId());

        model.addAttribute("wordList", list);

        return "thymeleaf/word/viewWordTestForm";
    }

    @PostMapping(path="/word/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> updateNextDateAndInsert(@CurrentUser User user
            , @RequestBody WordRequest.TestWord testWord) {
        this.wordSaveService.updatePassWord(user, testWord.getPass());
        return this.wordSaveService.insertFailWord(user, testWord.getFail());
    }

    @PostMapping(path="/word/answers/random", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void insertRandomFailWord(@CurrentUser User user
            , @RequestBody String[] answerIds) {
        this.wordSaveService.insertRandomFailWord(user, answerIds);
    }

    @GetMapping(value="/word/add")
    public String getWordAdd(AddWord word) {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());

        return "thymeleaf/word/createWordForm";
    }

    @GetMapping(value="/word/add/file")
    public String getWordAddByFileUpload()  {
        return "thymeleaf/word/createWordFileUploadForm";
    }

    @PostMapping(value="/word/add/upload", headers = "content-type=multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String createWordByFileUpload(@CurrentUser User user
            , @RequestParam(value="file") MultipartFile files) throws IOException {
        MultipartFile file = files;

        return this.wordSaveService.insertWordByFileUpload(user, file);
    }

    @PostMapping(value="/word/add", params={"addRow"})
    public String addRow(final AddWord word) {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());

        return "thymeleaf/word/createWordForm";
    }

    @PostMapping(value="/word/add", params={"removeRow"})
    public String removeRow(final AddWord word) {
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

    @PostMapping(value="/word/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateMeaning(@RequestBody Word word) {
        this.wordSaveService.updateMeaning(word);
    }

    @DeleteMapping(value="/word/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void delete(@RequestBody String[] rowIds) {
        this.wordSaveService.delete(rowIds);
    }
}