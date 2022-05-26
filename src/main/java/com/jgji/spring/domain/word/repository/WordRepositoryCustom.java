package com.jgji.spring.domain.word.repository;

import java.util.List;
import java.util.Map;

public interface WordRepositoryCustom {

    List<Map<String, Object>> findFrequentFailWord(int userId);
}
