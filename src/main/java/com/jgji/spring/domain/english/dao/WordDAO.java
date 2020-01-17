package com.jgji.spring.domain.english.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jgji.spring.domain.english.model.Word;

@Repository
public class WordDAO {
    @PersistenceContext
    private EntityManager em;
    
    public List<Word> getToDayWordList(Word word) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT                   ");
        sb.append("    W                    ");
        sb.append("FROM                     ");
        sb.append("    Word W               ");
        sb.append("WHERE                    ");
        sb.append("    NEXT_DATE <= :date   ");
        
        return em.createQuery(sb.toString(), Word.class)
        .setParameter("date", word.getNextDate())
        .getResultList();
    }
    
    @Transactional
    public boolean updatePassWord(String[] answerIds) {
        List<Word> list = getNextDateUpdateWordList(answerIds);
        
        for (Word word : list) {
            LocalDate nextDate = LocalDate.now();
            int currentLevel = word.getLevel();
            
            word.setNextDate(nextDate.plusDays(addDate(currentLevel)));
            word.setLevel(++currentLevel);
            
            em.merge(word);
        }
        em.flush();
        
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private List<Word> getNextDateUpdateWordList(String[] answerIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT                   ");
        sb.append("    *                    ");
        sb.append("FROM                     ");
        sb.append("    Word W               ");
        sb.append("WHERE                    ");
        
        for (int i = 0; i < answerIds.length; i++) {
            if (i != 0) {
                sb.append(" OR ");
            }
            sb.append("id = '").append(answerIds[i]).append("'");
        }
        
        return em.createNativeQuery(sb.toString(), Word.class).getResultList();
    }
    
    private int addDate(int level) {
        int addDate = 0;
        
        switch (level) {
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
