package com.jgji.spring.domain.word.model;

import java.util.ArrayList;
import java.util.List;

public class WordDTO {
    
    AddWord addWord;
    
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
}
