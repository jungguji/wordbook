package com.jgji.spring.domain.word.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.word.model.Word;

@Repository
public class WordDAO {
    @PersistenceContext
    private EntityManager em;
    
    public List<Word> findAllByUserId(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("    SELECT                      ");
        sb.append("        w                       ");
        sb.append("    FROM                        ");
        sb.append("        Word w                  ");
        sb.append("    JOIN                        ");
        sb.append("        w.user u                ");
        sb.append("    WHERE                       ");
        sb.append("        u.id = :userId          ");
        
        return em.createQuery(sb.toString(), Word.class)
            .setParameter("userId", userId)
            .getResultList();
    }
    
    public List<Word> getToDayWordList(Word word, String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("    SELECT                      ");
        sb.append("        w                       ");
        sb.append("    FROM                        ");
        sb.append("        Word w                  ");
        sb.append("    JOIN                        ");
        sb.append("        w.user u                ");
        sb.append("    WHERE                       ");
        sb.append("        u.id = :userId          ");
        sb.append("    AND                         ");
        sb.append("        w.nextDate <= :date     ");
        
        return em.createQuery(sb.toString(), Word.class)
            .setParameter("userId", userId)
            .setParameter("date", word.getNextDate())
            .getResultList();
    }
    
    public List<Word> getRandomWordList(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("    SELECT                ");
        sb.append("        w                 ");
        sb.append("    FROM                  ");
        sb.append("        Word w            ");
        sb.append("    JOIN                  ");
        sb.append("        w.user u          ");
        sb.append("    WHERE                 ");
        sb.append("        u.id = :userId    ");
        sb.append("    ORDER BY RAND()       ");
        
        return em.createQuery(sb.toString(), Word.class)
                .setParameter("userId", userId)
        .setMaxResults(15)
        .getResultList();
    }
    
    public List<Word> getRandomByAllWordList() {
        StringBuilder sb = new StringBuilder();
        sb.append("    SELECT                ");
        sb.append("        w                 ");
        sb.append("    FROM                  ");
        sb.append("        Word w            ");
        sb.append("    JOIN                  ");
        sb.append("        w.user u          ");
        sb.append("    ORDER BY RAND()       ");
        
        return em.createQuery(sb.toString(), Word.class)
                .setMaxResults(15)
                .getResultList();
    }
    
    
    @Transactional
    public boolean updateNextDateAndInsert(List<String> passWordList, List<String> failWordList, User user) {
        updatePassWord(passWordList, user.getId());
        insertFailWord(failWordList, user);
        
        return true;
    }
    
    @Transactional
    public boolean insertRandomFailWord(List<String> passWordList, List<String> failWordList, User user) {
        insertFailWord(failWordList, user);
        
        return true;
    }
    
    private void updatePassWord(List<String> passWordList, String userId) {
        List<Word> list = getWordList(passWordList, userId);
        
        LocalDate nextDate = LocalDate.now();
        for (Word word : list) {
            int currentLevel = word.getLevel();
            
            word.setNextDate(nextDate.plusDays(addDate(currentLevel)));
            word.setLevel(++currentLevel);
            
            em.merge(word);
        }
        em.flush();
    }
    
    private void insertFailWord(List<String> failWordList, User user) {
        final int PLUS_DAY = 1;
        List<Word> list = getWordList(failWordList, user.getId());
        
        LocalDate nextDate = LocalDate.now().plusDays(PLUS_DAY);
        
        for (Word word : list) {
            Word insertNewWord = new Word();
            insertNewWord.setWord(word.getWord());
            insertNewWord.setMeaning(word.getMeaning());
            insertNewWord.setNextDate(nextDate);
            insertNewWord.setUser(user);
            
            em.persist(insertNewWord);
        }
        
        em.flush();
    }
    
    private List<Word> getWordList(List<String> wordIdList, String userId) {
        if (wordIdList.isEmpty()) {
            return new ArrayList<Word>();
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("    SELECT                ");
        sb.append("        w                 ");
        sb.append("    FROM                  ");
        sb.append("        Word w            ");
        sb.append("    JOIN                  ");
        sb.append("        w.user u          ");
        sb.append("    WHERE                 ");
        sb.append("        u.id = :userId    ");
        sb.append("    AND                   ");
        
        for (int i = 0; i < wordIdList.size(); i++) {
            if (i != 0) {
                sb.append(" OR ");
            }
            sb.append("w.id = '").append(wordIdList.get(i)).append("'");
        }
        
        return em.createQuery(sb.toString(), Word.class)
                .setParameter("userId", userId)
                .getResultList();
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
    
    @Transactional
    public void insertWord(Word word) {
        em.persist(word);
        em.flush();
    }
    
    @Transactional
    public void updateMeaning(Word word) {
        em.merge(word);
        em.flush();
    }
    
    @Transactional
    public void delete(String[] rowIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("    DELETE FROM Word       ");
        sb.append("    WHERE id IN :rowIds    ");
        
        em.createNativeQuery(sb.toString())
        .setParameter("rowIds", Arrays.asList(rowIds))
        .executeUpdate();
        
        em.flush();
    }
}
