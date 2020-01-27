package com.jgji.spring.domain.word.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jgji.spring.domain.word.model.Word;

public interface WordService {
    
    List<Word> getToDayWordList(Word word);
    
    List<Word> getRandomWordList(Word word);

    boolean updateNextDateAndInsert(String[] answerIds);
    
    String insertWord(MultipartFile file) throws IOException;
}
