package com.jgji.spring.domain.word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.service.WordService;

@Controller
public class WordController {
    @Autowired
    private WordService service;
    
    @RequestMapping("/test")
    public String hello(Word word, Model model) {
        return "/english/test";
    }
    
    @RequestMapping("/word/test")
    public String getToDayWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        word.setNextDate(LocalDate.now());
        
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        String jsonText = objMapper.writeValueAsString(service.getToDayWordList(word));
        model.addAttribute("wordList", jsonText);
        return "thymeleaf/index";
    }
    
    @RequestMapping("/word/testttt")
    public String getToDayWordListaaa(Word word, Model model) throws ParseException, JsonProcessingException {
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
    
    @RequestMapping(value="/word/add")
    public String getWordAdd(Word word, Model model) throws ParseException, JsonProcessingException {
        return "thymeleaf/createWordForm";
    }
    
    @RequestMapping(value="/word/upload", method=RequestMethod.POST, headers = "content-type=multipart/form-data")
    @ResponseBody
    public String setWordAdd(@RequestParam(value="file") MultipartFile[] files) throws ParseException, IOException {
        System.out.println("TETsetsetset");
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            
        
            for (MultipartFile file : files) {
                isr = new InputStreamReader(file.getInputStream());
                br = new BufferedReader(isr);
                
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            
            if (isr != null) {
                isr.close();
            }
            
        }
        
        return "OK";
    }
}
