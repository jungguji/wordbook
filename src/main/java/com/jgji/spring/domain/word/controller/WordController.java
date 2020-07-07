package com.jgji.spring.domain.word.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgji.spring.domain.util.Utils;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO.AddWord;
import com.jgji.spring.domain.word.service.WordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class WordController {
    private WordService service;

    private WordController(WordService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Word word, Model model) {
        return "thymeleaf/index";
    }

    @GetMapping("/word/test")
    public String getToDayWordList(Model model) throws JsonProcessingException {
        ObjectMapper objMapper = Utils.getObjectMapperConfig();

        String jsonText = objMapper.writeValueAsString(this.service.findToDayWordList());

        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", true);
        
        return "thymeleaf/word/viewWordTestForm";
    }

    @GetMapping("/word/test/random")
    public String getRandomByUserWordList(Model model) throws JsonProcessingException {
        ObjectMapper objMapper = Utils.getObjectMapperConfig();
        
        List<Word> wordList = service.getRandomWordList();
        
        boolean isExist = !ObjectUtils.isEmpty(wordList);

        String jsonText = objMapper.writeValueAsString(wordList);
        
        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", isExist);
        
        return "thymeleaf/word/viewWordTestForm";
    }
    
    @GetMapping(value="/word/test/random", params= {"all"})
    public String getRandomByAllWordList(Model model) throws JsonProcessingException {
        ObjectMapper objMapper = Utils.getObjectMapperConfig();
        
        String jsonText = objMapper.writeValueAsString(service.getRandomByAllWordList());

        model.addAttribute("wordList", jsonText);
        model.addAttribute("isExist", true);
        
        return "thymeleaf/word/viewWordTestForm";
    }
    
    @PostMapping(path="/word/answers", produces = "application/json")
    @ResponseBody
    public boolean updateNextDateAndInsert(@RequestParam("pass") String pass, @RequestParam("fail") String fail) {
        System.out.println("test");
        return true;
    }
    
    @PostMapping(path="/word/answers/random", produces = "application/json")
    @ResponseBody
    public boolean insertRandomFailWord(@RequestBody String[] answerIds) {
        return service.insertRandomFailWord(answerIds);
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
    public String createWordByFileUpload(@RequestParam(value="file") MultipartFile files) throws IOException {
        MultipartFile file = files;

        return service.insertWordByFileUpload(file);
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