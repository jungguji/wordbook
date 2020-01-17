package com.jgji.spring.domain.english.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jgji.spring.domain.english.dao.WordDAO;
import com.jgji.spring.domain.english.model.Word;

@Service("wordService")
public class WordServiceImpl implements WordService{
    @Autowired
    private WordDAO wordDAO;
    
    public List<Word> getToDayWordList(Word word) {
        return wordDAO.getToDayWordList(word);
    }

    public boolean updatePassWord(String[] answerIds) {
        return wordDAO.updatePassWord(answerIds);
    }
}
