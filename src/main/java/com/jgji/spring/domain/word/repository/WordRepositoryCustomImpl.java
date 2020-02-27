package com.jgji.spring.domain.word.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.word.model.Word;

public class WordRepositoryCustomImpl implements WordRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    
    public void updateSuccessWord(List<Word> list, String userId) {
        LocalDate nextDate = LocalDate.now();
        
        for (Word word : list) {
            int currentLevel = word.getLevel();
            
            word.setNextDate(nextDate.plusDays(addDate(currentLevel)));
            word.setLevel(++currentLevel);
            
            em.merge(word);
        }
        em.flush();
    }
    
    public void insertFailWord(List<Word> list, User user) {
        final int PLUS_DAY = 1;
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
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findFrequentFailWord(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("    SELECT                    ");
        sb.append("        word AS word          ");
        sb.append("        , COUNT(1) AS qty     ");
        sb.append("    FROM                      ");
        sb.append("        word                  ");
        sb.append("    WHERE                     ");
        sb.append("        users_id = :userId    ");
        sb.append("    GROUP BY                  ");
        sb.append("        word                  ");
        sb.append("    ORDER BY qty DESC         ");
        sb.append("    LIMIT 10;                 ");
        
        List<Object[]> result = em.createNativeQuery(sb.toString())
        .setParameter("userId", userId)
        .getResultList();
        
        List<String> columns = new ArrayList<String>();
        columns.add("word");
        columns.add("qty");
        
        return convertMapList(result, columns);
    }
    
    public List<Map<String, Object>> convertMapList(List<Object[]> resultList, List<String> columns) {
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        Map<String, Object> itemMap;
        
        Iterator<Object[]> it = resultList.iterator();
        while(it.hasNext()) {
            Object[] item = it.next();
            
            itemMap = new HashMap<String, Object>();
            int idx = 0;
            
            for(String key : columns) {
                itemMap.put(key, item[idx++]);
            }
            mapList.add(itemMap);
        }
        return mapList;
    }
}
