package com.jgji.spring.domain.word.dto;

import com.jgji.spring.domain.word.domain.Word;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class WordResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TodayWord {
        private int id;
        private String word;
        private String meaning;

        @Builder
        public TodayWord(int id, String word, String meaning) {
            this.id = id;
            this.word = word;
            this.meaning = meaning;
        }

        public static TodayWord of(Word word) {
            return TodayWord.builder()
                    .id(word.getId())
                    .word(word.getWord())
                    .meaning(word.getMeaning())
                    .build();
        }

        public static List<TodayWord> ofList(List<Word> words) {
            final int LIMIT_WORD = 15;
            List<TodayWord> list = new ArrayList<>();

            int count = 0;
            for (Word word : words) {
                if (count == LIMIT_WORD) {
                    break;
                }

                list.add(of(word));

                ++count;
            }
            return list;
        }
    }
}
