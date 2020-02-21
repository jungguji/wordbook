package com.jgji.spring.domain.word.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.repository.WordMapper;

@Repository
public class WordDAO {
    @Autowired
    private WordMapper wordMapper;
    
//    
//    private void insertFailWord(List<String> failWordList, User user) {
//        final int PLUS_DAY = 1;
//        List<Word> list = getWordList(failWordList, user.getId());
//        
//        LocalDate nextDate = LocalDate.now().plusDays(PLUS_DAY);
//        
//        for (Word word : list) {
//            Word insertNewWord = new Word();
//            insertNewWord.setWord(word.getWord());
//            insertNewWord.setMeaning(word.getMeaning());
//            insertNewWord.setNextDate(nextDate);
//            insertNewWord.setUser(user);
//            
//            em.persist(insertNewWord);
//        }
//        
//        em.flush();
//    }
//    
//    
//    @Transactional
//    public void insertWord(Word word) {
//        em.persist(word);
//        em.flush();
//    }
//    
//    @Transactional
//    public void updateMeaning(Word word) {
//        em.merge(word);
//        em.flush();
//    }
//    
//    @Transactional
//    public void delete(String[] rowIds) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("    DELETE FROM Word       ");
//        sb.append("    WHERE id IN :rowIds    ");
//        
//        em.createNativeQuery(sb.toString())
//        .setParameter("rowIds", Arrays.asList(rowIds))
//        .executeUpdate();
//        
//        em.flush();
//    }
//    
//    public List<Map<String, Object>> getFrequentFailWord(String userId) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("    SELECT                    ");
//        sb.append("        word AS word          ");
//        sb.append("        , COUNT(1) AS qty     ");
//        sb.append("    FROM                      ");
//        sb.append("        word                  ");
//        sb.append("    WHERE                     ");
//        sb.append("        users_id = :userId    ");
//        sb.append("    GROUP BY                  ");
//        sb.append("        word                  ");
//        sb.append("    ORDER BY qty DESC         ");
//        sb.append("    LIMIT 10;                 ");
//        
//        List<Object[]> result = em.createNativeQuery(sb.toString())
//        .setParameter("userId", userId)
//        .getResultList();
//        
//        List<String> columns = new ArrayList<String>();
//        columns.add("word");
//        columns.add("qty");
//        
//        return convertMapList(result, columns);
//    }
//    
//    public List<Map<String, Object>> convertMapList(List<Object[]> resultList, List<String> columns) {
//        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
//        Map<String, Object> itemMap;
//        
//        Iterator<Object[]> it = resultList.iterator();
//        while(it.hasNext()) {
//            Object[] item = it.next();
//            
//            itemMap = new HashMap<String, Object>();
//            int idx = 0;
//            
//            for(String key : columns) {
//                itemMap.put(key, item[idx++]);
//            }
//            mapList.add(itemMap);
//        }
//        return mapList;
//    }
}
