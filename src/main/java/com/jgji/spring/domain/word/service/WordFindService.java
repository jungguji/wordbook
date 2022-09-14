package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.dto.WordResponse;
import com.jgji.spring.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WordFindService {

    private final WordRepository wordRepository;

    public List<Word> findAllByUserId(int userId) {
        return wordRepository.findByUserId(userId);
    }

    public List<WordResponse.TodayWord> findToDayTestWordList(int userId) {

        List<Word> todayWords = Optional.ofNullable(wordRepository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now()))
                .orElseGet(() -> Optional.ofNullable(wordRepository.findByUserIdOrderByRandom(userId))
                        .orElseGet(() -> wordRepository.findOrderByRandom()));

        return WordResponse.TodayWord.ofList(todayWords);
    }

    public List<Map<String, Object>> findFrequentFailWord(int userId) {
        return this.wordRepository.findFrequentFailWord(userId);
    }
}
