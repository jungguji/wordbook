package com.jgji.spring.domain.word.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO.AddWord;

public interface WordService {
    
    List<Word> findAllByUserId();
    
    List<Word> findToDayWordList(Word word);
    
    List<Word> getRandomWordList();
    
    List<Word> getRandomByAllWordList();

    boolean updateNextDateAndInsert(String[] answerIds);
    
    boolean insertRandomFailWord(String[] answerIds);
    
    String insertWordByFileUpload(MultipartFile file) throws IOException;
    
    String insertWord(AddWord word);
    
    boolean updateMeaning(Word word);
    
    void delete(String[] args);
    
    List<Map<String, Object>> getFrequentFailWord(String userId);
    
    BindingResult getCreateWordBindingResult(AddWord word, BindingResult bindingResult);
}
