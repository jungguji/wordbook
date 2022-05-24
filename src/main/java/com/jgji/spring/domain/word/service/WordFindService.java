package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.dto.WordResponse;
import com.jgji.spring.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WordFindService {

    private final WordRepository wordRepository;

    public List<Word> findAllByUserId(int userId) {
        return wordRepository.findByUserId(userId);
    }

    public List<Word> findToDayWordList(int userId) {
        return wordRepository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now());
    }

    public List<WordResponse.TodayWord> findToDayTestWordList(int userId) {

        List<Word> todayWords = Optional.ofNullable(wordRepository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now()))
                .orElseGet(() -> Optional.ofNullable(wordRepository.findByUserIdOrderByRandom(userId))
                        .orElseGet(() -> wordRepository.findOrderByRandom()));

        return WordResponse.TodayWord.ofList(todayWords);
    }

    public List<Word> findRandomWordList(int userId) {
        return this.wordRepository.findByUserIdOrderByRandom(userId);
    }
}
