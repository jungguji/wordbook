package com.jgji.spring.domain.word.domain;

import com.jgji.spring.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor
@Getter
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Builder
    public Word(String word, String meaning, LocalDate nextDate, int level, User user) {
        this.word = word;
        this.meaning = meaning;
        this.nextDate = nextDate;
        this.level = level;
        this.user = user;
    }

    public String toString() {
        return "id = " + this.id + " word = " + this.word + " meaning = " + meaning + " next_date = " + this.nextDate;
    }

    public void updateDate(LocalDate nextDate) {
        this.nextDate = nextDate;
    }

    public void updateLevel(int level) {
        this.level = level;
    }
    
}
