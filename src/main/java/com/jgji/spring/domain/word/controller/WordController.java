package com.jgji.spring.domain.word.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgji.spring.domain.util.Utils;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO.AddWord;
import com.jgji.spring.domain.word.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class WordController {
    @Autowired
    private WordService service;

    @Autowired
    Environment env;

    @GetMapping("/")
    @ResponseBody
    public String home(Word word, Model model) throws JsonProcessingException {
        return "thymeleaf/index";
    }
    
    @GetMapping("/word/test")
    public String getToDayWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        word.setNextDate(LocalDate.now());
        
        ObjectMapper objMapper = Utils.getObjectMapperConfig();

        String jsonText = objMapper.writeValueAsString(service.findToDayWordList(word));
        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", true);
        
        return "thymeleaf/word/viewWordTestForm";
    }

    @GetMapping("/word/test/random")
    public String getRandomByUserWordList(Word word, Model model) throws ParseException, JsonProcessingException {
        ObjectMapper objMapper = Utils.getObjectMapperConfig();
        
        List<Word> wordList = service.getRandomWordList();
        
        boolean isExist = true;
        if (ObjectUtils.isEmpty(wordList)) {
            isExist = false;
        }
        
        String jsonText = objMapper.writeValueAsString(wordList);
        
        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", isExist);
        
        return "thymeleaf/word/viewWordTestForm";
    }
    
    @GetMapping(value="/word/test/random", params= {"all"})
    public String getRandomByAllWordList(Model model) throws ParseException, JsonProcessingException {
        ObjectMapper objMapper = Utils.getObjectMapperConfig();
        
        String jsonText = objMapper.writeValueAsString(service.getRandomByAllWordList());
        
        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", true);
        
        return "thymeleaf/word/viewWordTestForm";
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
    public String getWordAdd(AddWord word, Model model) throws ParseException, JsonProcessingException {
        word.getWords().add(new Row());
        word.getMeanings().add(new Row());
        
        return "thymeleaf/word/createWordForm";
    }
    
    @GetMapping(value="/word/add/file")
    public String getWordAddByFileUpload(Word word, Model model) throws ParseException, JsonProcessingException {
        return "thymeleaf/word/createWordFileUploadForm";
    }
    
    @RequestMapping(value="/word/add/upload", method=RequestMethod.POST, headers = "content-type=multipart/form-data")
    @ResponseBody
    public String createWordByFileUpload(@RequestParam(value="file") MultipartFile files) throws ParseException, IOException {
        MultipartFile file = files;
        String words = service.insertWordByFileUpload(file);
        
        return words;
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
    public String createWord(@Valid AddWord word, BindingResult bindingResult) {
        BindingResult result = service.getCreateWordBindingResult(word, bindingResult);
        
        if (result.hasErrors()) {
            return "thymeleaf/word/createWordForm";
        }
        
        service.insertWord(word);
        return "thymeleaf/index";
    }
    
    @PostMapping(value="/word/update", produces = "application/json")
    @ResponseBody
    public boolean updateMeaning(@RequestBody Word word) {
        return service.updateMeaning(word);
    }
    
    @DeleteMapping(value="/word/delete", produces = "application/json")
    @ResponseBody
    public String delete(@RequestBody String[] rowIds) {
        service.delete(rowIds);
        return "good";
    }
    
}
