package com.jgji.spring.domain.word.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class WordDTO {

    public AddWord getAddWord() {
        return new AddWord();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AddWord {
        private String word;
        private String meaning;
        private List<Row> words = new ArrayList<>();
        private List<Row> meanings = new ArrayList<>();

        @Builder
        public AddWord(String word, String meaning) {
            this.word = word;
            this.meaning = meaning;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResponseWord {
        private int id;
        private String word;
        private String meaning;

        public ResponseWord(int id, String word, String meaning) {
            this.id = id;
            this.word = word;
            this.meaning = meaning;
        }
    }
}
