package com.jgji.spring.domain.english.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String word;
    private String meaning;
    private LocalDate nextDate;
    private int level;
    
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
    
    public String toString() {
        return "id = " + this.id + " word = " + this.word + " next_date = " + this.nextDate;
    }
    
}
