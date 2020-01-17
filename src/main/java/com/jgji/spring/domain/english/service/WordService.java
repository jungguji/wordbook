package com.jgji.spring.domain.english.service;

import java.util.List;

import com.jgji.spring.domain.english.model.Word;

public interface WordService {
    
    List<Word> getToDayWordList(Word word);

    boolean updatePassWord(String[] answerIds);
}
