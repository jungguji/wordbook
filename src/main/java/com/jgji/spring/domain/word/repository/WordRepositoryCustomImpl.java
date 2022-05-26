package com.jgji.spring.domain.word.repository;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.word.domain.Word;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.*;

public class WordRepositoryCustomImpl implements WordRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findFrequentFailWord(int userId) {
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
        
        List<String> columns = new ArrayList<>();
        columns.add("word");
        columns.add("qty");
        
        return convertMapList(result, columns);
    }
    
    public List<Map<String, Object>> convertMapList(List<Object[]> resultList, List<String> columns) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String, Object> itemMap;
        
        Iterator<Object[]> it = resultList.iterator();
        while(it.hasNext()) {
            Object[] item = it.next();
            
            itemMap = new HashMap<>();
            int idx = 0;
            
            for(String key : columns) {
                itemMap.put(key, item[idx++]);
            }
            mapList.add(itemMap);
        }
        return mapList;
    }
}
