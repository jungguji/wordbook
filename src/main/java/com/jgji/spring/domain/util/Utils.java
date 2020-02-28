package com.jgji.spring.domain.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
}
