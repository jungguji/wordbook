package com.jgji.spring.domain.english;

import java.text.ParseException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jgji.spring.domain.english.model.Word;
import com.jgji.spring.domain.english.service.WordService;

@Controller
public class WordController {
    @Autowired
    private WordService service;
    
    @RequestMapping("/")
    public String hello(Word word, Model model) {
        return "hello, world";
    }
    
    @RequestMapping("/english/wordlist")
    public String getToDayWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        
        word.setNextDate(LocalDate.now());
        
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        String jsonText = objMapper.writeValueAsString(service.getToDayWordList(word));
        model.addAttribute("wordList", jsonText);
        return "/english/getWordList";
    }
    
    @RequestMapping(value="/english/answers", method=RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public boolean updateNextDateAndInsert(@RequestBody String[] answerIds) throws ParseException, JsonProcessingException {
        boolean result = service.updateNextDateAndInsert(answerIds);
        
        return result;
    }
}
