package com.jgji.spring.domain.word.dto;


import com.jgji.spring.domain.word.domain.Row;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class WordRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TestWord {

        private List<Integer> pass;
        private List<Integer> fail;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AddWord {
        private String word;
        private String meaning;
        private List<Row> words = new ArrayList<>();
        private List<Row> meanings = new ArrayList<>();

        @Builder
        public AddWord(String word, String meaning, List<Row> words, List<Row> meanings) {
            this.word = word;
            this.meaning = meaning;
            this.words = words;
            this.meanings = meanings;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ResponseWord {
        private int id;
        private String word;
        private String meaning;
    }
}
