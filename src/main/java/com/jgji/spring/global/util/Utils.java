package com.jgji.spring.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.*;

public class Utils {

    public static List<Map<String, Object>> convertListMap(List<Object[]> resultList, List<String> columns) {
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
    
    public static ObjectMapper getObjectMapperConfig() {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());
        objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        return objMapper;
    }
    
    public static String returnJsonMsg(String msg) {
        String message = "";
        try {
            ObjectMapper objMapper = getObjectMapperConfig();
            message = objMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return message;
    }
    
    
}
