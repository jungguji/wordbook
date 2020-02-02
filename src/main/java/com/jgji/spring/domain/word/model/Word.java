package com.jgji.spring.domain.word.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jgji.spring.domain.user.model.User;

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
    
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;
    
    @Transient
    private List<Row> words = new ArrayList<Row>();
    
    @Transient
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
    
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<Row> getWords() {
        return words;
    }
    public List<Row> getMeanings() {
        return meanings;
    }
    
    public String toString() {
        return "id = " + this.id + " word = " + this.word + " next_date = " + this.nextDate;
    }
    
}
