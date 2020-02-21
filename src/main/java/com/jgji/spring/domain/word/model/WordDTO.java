package com.jgji.spring.domain.word.model;

public class WordDTO {
    
    AddWord addWord;
    
    public AddWord getAddWord() {
        return new AddWord();
    }

    public static class AddWord {
        private String word;
        private String meaning;
        
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
    }
}
