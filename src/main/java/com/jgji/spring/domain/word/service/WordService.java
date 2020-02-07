package com.jgji.spring.domain.word.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jgji.spring.domain.word.model.Word;

public interface WordService {
    
    List<Word> findAllByUserId();
    
    List<Word> getToDayWordList(Word word);
    
    List<Word> getRandomWordList();

    boolean updateNextDateAndInsert(String[] answerIds);
    
    boolean insertRandomFailWord(String[] answerIds);
    
    String insertWord(MultipartFile file) throws IOException;
    
    String insertWord(Word word);
    
    boolean updateMeaning(Word word);
    
    void delete(String[] args);
}
