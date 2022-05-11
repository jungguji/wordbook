package com.jgji.spring.domain.word.repository;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.word.domain.Word;

import java.util.List;
import java.util.Map;

public interface WordRepositoryCustom {

    void updateSuccessWord(List<Word> list, int userId);
    
    List<String> insertFailWord(List<Word> list, User user);
    
    List<Map<String, Object>> findFrequentFailWord(int userId);
}
