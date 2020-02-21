package com.jgji.spring.domain.word.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.jgji.spring.domain.word.model.Word;

public interface WordService {
    
    List<Word> findAllByUserId();
    
//    List<Word> getToDayWordList(Word word);
//    
//    List<Word> getRandomWordList();
//    
//    List<Word> getRandomByAllWordList();
//
    String updateNextDateAndInsert(String[] answerIds);
//    
//    boolean insertRandomFailWord(String[] answerIds);
//    
//    String insertWord(MultipartFile file) throws IOException;
//    
//    String insertWord(Word word);
//    
//    boolean updateMeaning(Word word);
//    
//    void delete(String[] args);
//    
//    List<Map<String, Object>> getFrequentFailWord(String userId);
//    
//    BindingResult getCreateWordBindingResult(Word word, BindingResult bindingResult);
}
