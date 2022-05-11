package com.jgji.spring.domain.word.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class WordDTO {
    
    public AddWord getAddWord() {
        return new AddWord();
    }

    public static class AddWord {
        private String word;
        private String meaning;
        private List<Row> words = new ArrayList<Row>();
        private List<Row> meanings = new ArrayList<Row>();
        
        public String getWord() {
            return word;
        }
        public void setWord(String word) {
            this.word = word;
        }
        public String getMeaning() {
            return meaning;
        }
        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }
        
        public List<Row> getWords() {
            return words;
        }
        public List<Row> getMeanings() {
            return meanings;
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
