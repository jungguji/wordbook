package com.jgji.spring.domain.word.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    
    @GetMapping("/word/test")
    public String getToDayWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        word.setNextDate(LocalDate.now());
        
        ObjectMapper objMapper = getObjectMapperConfig();
        
        String jsonText = objMapper.writeValueAsString(service.getToDayWordList(word));
        model.addAttribute("wordList", jsonText);
        
        return "thymeleaf/viewWordTest";
    }

    @GetMapping("/word/test/random")
    public String getrandomWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        ObjectMapper objMapper = getObjectMapperConfig();
        
        String jsonText = objMapper.writeValueAsString(service.getRandomWordList(word));
        
        model.addAttribute("wordList", jsonText);
        
        return "thymeleaf/viewWordTest";
    }
    
    private ObjectMapper getObjectMapperConfig() {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        return objMapper;
    }
    
    @PostMapping(path="/word/answers", produces = "application/json")
    @ResponseBody
    public boolean updateNextDateAndInsert(@RequestBody String[] answerIds) throws ParseException, JsonProcessingException {
        boolean result = service.updateNextDateAndInsert(answerIds);
        
        return result;
    }
    
    @PostMapping(path="/word/answers/random", produces = "application/json")
    @ResponseBody
    public boolean insertRandomFailWord(@RequestBody String[] answerIds) throws ParseException, JsonProcessingException {
        boolean result = service.insertRandomFailWord(answerIds);
        
        return result;
    }
    
    @GetMapping(value="/word/add")
    public String getWordAdd(Word word, Model model) throws ParseException, JsonProcessingException {
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
        String words = service.insertWord(file);
        
        return words;
    }
    
    @PostMapping(value="/word/add", params= {"addRow"})
    public String addRow(final Word word, final BindingResult bindingResult) {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());
        
        return "thymeleaf/createWordForm";
    }
    
    @PostMapping(value="/word/add", params= {"save"})
    public String createWord(@Valid Word word, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "thymeleaf/createWordForm";
        }
        service.insertWord(word);
        return "thymeleaf/index";
    }
    
}
