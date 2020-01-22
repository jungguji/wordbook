package com.jgji.spring.domain.word.service;

import java.util.List;

import com.jgji.spring.domain.word.model.Word;

public interface WordService {
    
    List<Word> getToDayWordList(Word word);

    boolean updateNextDateAndInsert(String[] answerIds);
}
