package com.jgji.spring.domain.word.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jgji.spring.domain.utils.Utils;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.service.WordService;

@Controller
public class WordController {
    @Autowired
    private WordService service;
    
    @GetMapping("/")
    public String home(Word word, Model model) {
        return "thymeleaf/index";
    }
    
    @GetMapping("/mybatis")
    @ResponseBody
    public String mybatis() throws JsonProcessingException {
        String[] args = new String[5];
        
        args[0] = "23_0";
        args[1] = "23_1";
        args[2] = "24_1";
        args[3] = "24_0";
        args[4] = "25_0";
        
        ObjectMapper objMapper = getObjectMapperConfig();
        return objMapper.writeValueAsString(service.updateNextDateAndInsert(args));
    }
    
    @GetMapping("/word/test")
    public String getToDayWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        word.setNextDate(LocalDate.now());
        
        ObjectMapper objMapper = getObjectMapperConfig();
        
        String jsonText = objMapper.writeValueAsString(service.getToDayWordList(word));
        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", true);
        
        return "thymeleaf/viewWordTestForm";
    }

    @GetMapping("/word/test/random")
    public String getRandomByUserWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        ObjectMapper objMapper = getObjectMapperConfig();
        
        List<Word> wordList = service.getRandomWordList();
        
        boolean isExist = true;
        if (ObjectUtils.isEmpty(wordList)) {
            isExist = false;
        }
        
        String jsonText = objMapper.writeValueAsString(wordList);
        
        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", isExist);
        
        return "thymeleaf/viewWordTestForm";
    }
    
    @GetMapping(value="/word/test/random", params= {"all"})
    public String getRandomByAllWordList(Model model) throws ParseException, JsonProcessingException {
        ObjectMapper objMapper = getObjectMapperConfig();
        
        String jsonText = objMapper.writeValueAsString(service.getRandomByAllWordList());
        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", true);
        
        return "thymeleaf/viewWordTestForm";
    }
    
    private ObjectMapper getObjectMapperConfig() {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        return objMapper;
    }
    
    @PostMapping(path="/word/answers", produces = "application/json")
    @ResponseBody
    public String updateNextDateAndInsert(@RequestBody String[] answerIds) throws ParseException, JsonProcessingException {
        String result = service.updateNextDateAndInsert(answerIds);
        
        return result;
    }
    
    @PostMapping(path="/word/answers/random", produces = "application/json")
    @ResponseBody
    public String insertRandomFailWord(@RequestBody String[] answerIds) throws ParseException, JsonProcessingException {
        String result = service.insertRandomFailWord(answerIds);
        
        return Utils.returnJsonMsg(result);
    }
    
    @GetMapping(value="/word/add")
    public String getWordAdd(Word word, Model model) throws ParseException, JsonProcessingException {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());
        
        return "thymeleaf/createWordForm";
    }
    
    @GetMapping(value="/word/add/file")
    public String getWordAddByFileUpload(Word word, Model model) throws ParseException, JsonProcessingException {
        return "thymeleaf/createWordFileUploadForm";
    }
    
    @PostMapping(path="/word/add/upload", headers = "content-type=multipart/form-data")
    @ResponseBody
    public String createWordByFileUpload(@RequestParam(value="file") MultipartFile[] files) throws ParseException, IOException {
        MultipartFile file = files[0];
        String words = service.insertWordByFileUpload(file);
        
        return words;
    }
    
    @PostMapping(value="/word/add", params={"addRow"})
    public String addRow(final Word word, final BindingResult bindingResult) {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());
        
        return "thymeleaf/createWordForm";
    }
    
    @PostMapping(value="/word/add", params={"removeRow"})
    public String removeRow(final Word word, final BindingResult bindingResult) {
        if (word.getWords().size() > 1) {
            word.getWords().remove(word.getWords().size()-1);
            word.getMeanings().remove(word.getMeanings().size()-1);
        }
        
        return "thymeleaf/createWordForm";
    }
    
    @PostMapping(value="/word/add", params= {"save"})
    public String createWord(@Valid Word word, BindingResult bindingResult) {
        bindingResult = service.getCreateWordBindingResult(word, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "thymeleaf/createWordForm";
        }
        
        service.insertWord(word);
        return "thymeleaf/index";
    }
    
    @PostMapping(value="/word/update", produces = "application/json")
    @ResponseBody
    public boolean updateMeaning(@RequestBody Word word) {
        boolean isResult = service.updateMeaning(word);
        return isResult;
    }
    
    @DeleteMapping(value="/word/delete", produces = "application/json")
    @ResponseBody
    public String delete(@RequestBody String[] rowIds) {
        service.delete(rowIds);
        return "good";
    }
    
}
