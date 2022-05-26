package com.jgji.spring.domain.word.domain;

import com.jgji.spring.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@NoArgsConstructor
@Table(name = "word")
@Entity
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

    public void levelUp() {
        this.nextDate = this.nextDate.plusDays(getAddDate());
        this.level += 1;
    }

    private int getAddDate() {
        int addDate;

        switch (this.level) {
            case 0:
            case 1:
                addDate = 1;
                break;
            case 2:
                addDate = 3;
                break;
            case 3:
                addDate = 7;
                break;
            case 4:
                addDate = 15;
                break;
            case 5:
                addDate = 30;
                break;
            default:
                addDate = 60;
                break;
        }

        return addDate;
    }
    
}
