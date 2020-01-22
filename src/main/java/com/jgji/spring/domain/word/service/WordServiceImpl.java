package com.jgji.spring.domain.word.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jgji.spring.domain.word.dao.WordDAO;
import com.jgji.spring.domain.word.model.Word;

@Service("wordService")
public class WordServiceImpl implements WordService{
    @Autowired
    private WordDAO wordDAO;
    
    public List<Word> getToDayWordList(Word word) {
        return wordDAO.getToDayWordList(word);
    }

    public boolean updateNextDateAndInsert(String[] answerIds) {
        List<String> passWordList = new ArrayList<String>();
        List<String> failWordList = new ArrayList<String>();
        
        for (int i = 0; i < answerIds.length; i++) {
            if (answerIds[i].endsWith("_1")) {
                passWordList.add(answerIds[i].split("_")[0]);
            } else {
                String id = answerIds[i].split("_")[0];
                
                if (!failWordList.contains(id)) {
                    failWordList.add(id);
                }
            }
        }
        
        return wordDAO.updateNextDateAndInsert(passWordList, failWordList);
    }
}
