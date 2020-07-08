package com.jgji.spring.domain.word.repository;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.word.model.Word;

import java.util.List;
import java.util.Map;

public interface WordRepositoryCustom {

    void updateSuccessWord(List<Word> list, String userId);
    
    List<String> insertFailWord(List<Word> list, User user);
    
    List<Map<String, Object>> findFrequentFailWord(String userId);
}
