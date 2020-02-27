package com.jgji.spring.domain.word.repository;

import java.util.List;
import java.util.Map;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.word.model.Word;

public interface WordRepositoryCustom {

    void updateSuccessWord(List<Word> list, String userId);
    
    void insertFailWord(List<Word> list, User user);
    
    List<Map<String, Object>> findFrequentFailWord(String userId);
}
