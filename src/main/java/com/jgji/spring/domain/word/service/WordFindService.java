package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.domain.WordDTO;
import com.jgji.spring.domain.word.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WordFindService {

    private final WordRepository wordRepository;

    private final static int LIMIT_WORD = 15;

    public List<Word> findAllByUserId(int userId) {
        return wordRepository.findByUserId(userId);
    }

    public List<Word> findToDayWordList(int userId) {
        return wordRepository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now());
    }

    public List<WordDTO.ResponseWord> findToDayTestWordList(int userId) {
        List<Word> todayList = wordRepository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now());
        if (todayList.isEmpty()) {
            todayList = wordRepository.findByUserIdOrderByRandom(userId);
        }

        if (todayList.isEmpty()) {
            todayList = wordRepository.findOrderByRandom();
        }

//        List<Word> todayList = Optional.ofNullable(repository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now()))
//                .or(() -> Optional.ofNullable(repository.findByUserIdOrderByRandom(userId)))
//                .orElse(repository.findOrderByRandom());

        List<WordDTO.ResponseWord> dtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        int count = 0;
        for (Word word : todayList) {
            if (count == LIMIT_WORD) {
                break;
            }

            WordDTO.ResponseWord dto = modelMapper.map(word, WordDTO.ResponseWord.class);
            dtoList.add(dto);
            count++;
        }

        return dtoList;
    }

    public List<Word> findRandomWordList(int userId) {
        return this.wordRepository.findByUserIdOrderByRandom(userId);
    }

    public List<Word> findRandomByAllWordList() {
        return wordRepository.findOrderByRandom();
    }
}
