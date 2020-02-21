package com.jgji.spring.domain.word.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.jgji.spring.domain.user.model.User;

@Alias("word")
public class Word {
    private int id;
    private String word;
    private String meaning;
    private LocalDate nextDate;
    private int level;
    private String usersId;
    
    private List<Row> words = new ArrayList<Row>();
    
    private List<Row> meanings = new ArrayList<Row>();
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
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
    
    public LocalDate getNextDate() {
        return nextDate;
    }
    public void setNextDate(LocalDate nextDate) {
        this.nextDate = nextDate;
    }
    
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    
    public String getUsersId() {
        return usersId;
    }
    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }
    public List<Row> getWords() {
        return words;
    }
    public List<Row> getMeanings() {
        return meanings;
    }
    
    public String toString() {
        return "id = " + this.id + " word = " + this.word + " meaning = " + meaning + " next_date = " + this.nextDate;
    }
    
}
