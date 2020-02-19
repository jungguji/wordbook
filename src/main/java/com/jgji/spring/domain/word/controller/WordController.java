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
import org.springframework.util.StringUtils;
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
        String words = service.insertWord(file);
        
        return words;
    }
    
    @PostMapping(value="/word/add", params={"addRow"})
    public String addRow(final Word word, final BindingResult bindingResult) {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());
        
        return "thymeleaf/createWordForm";
    }
    
    @PostMapping(value="/word/add", params= {"save"})
    public String createWord(@Valid Word word, BindingResult bindingResult) {
        bindingResult = isValidation(word, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "thymeleaf/createWordForm";
        }
        service.insertWord(word);
        return "thymeleaf/index";
    }
    
    private BindingResult isValidation(Word word, BindingResult bindingResult) {
        StringBuilder wordRow = new StringBuilder();
        StringBuilder meaningRow = new StringBuilder();
        
        int wordCount = word.getWords().size();
        for (int i = 0; i < wordCount; i++) {
            int wordsErrorCount = bindingResult.getFieldErrorCount("words");
            if (StringUtils.isEmpty(word.getWords().get(i).getText()) && wordsErrorCount == 0) {
                
                if (wordRow.length() != 0) {
                    wordRow.append(", ");
                }
                wordRow.append((i+1));
            }
            
            int meaningsErrorCount = bindingResult.getFieldErrorCount("meanings");
            if (StringUtils.isEmpty(word.getMeanings().get(i).getText()) && meaningsErrorCount == 0) {
                
                if (meaningRow.length() != 0) {
                    meaningRow.append(", ");
                }
                
                meaningRow.append((i+1));
            }
        }
            
        if (wordRow.length() != 0) {
            wordRow.append("행에 단어가 비어 있습니다.");
            
            bindingResult.rejectValue("words", "words", wordRow.toString());
        }
        
        if (meaningRow.length() != 0) {
            meaningRow.append("행에 뜻이 비어 있습니다.");
            
            bindingResult.rejectValue("words", "words", meaningRow.toString());
        }
        
        return bindingResult;
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
