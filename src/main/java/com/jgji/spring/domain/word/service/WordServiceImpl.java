package com.jgji.spring.domain.word.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.dao.WordDAO;
import com.jgji.spring.domain.word.model.Word;

@Service("wordService")
public class WordServiceImpl implements WordService{
    final static String PASS_LIST = "pass";
    final static String FAIL_LIST = "fail";
    
    @Autowired
    private WordDAO wordDAO;
    
    @Autowired
    private UserService userService;
    
    public List<Word> getToDayWordList(Word word) {
        String userId = getUserId();
        
        return wordDAO.getToDayWordList(word, userId);
    }
    
    public List<Word> getRandomWordList(Word word) {
        String userId = getUserId();
        
        return wordDAO.getRandomWordList(word, userId);
    }
    
    private String getUserId() {
        return userService.getUserIdByUserName(userService.getCurrentUserName());
    }

    public boolean updateNextDateAndInsert(String[] answerIds) {
        Map<String, List<String>> passAndFail = getPassAndFailWordList(answerIds);
        List<String> passWordList = passAndFail.get(PASS_LIST);
        List<String> failWordList = passAndFail.get(FAIL_LIST);
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        
        return wordDAO.updateNextDateAndInsert(passWordList, failWordList, user);
    }
    
    public boolean insertRandomFailWord(String[] answerIds) {
        Map<String, List<String>> passAndFail = getPassAndFailWordList(answerIds);
        List<String> passWordList = passAndFail.get(PASS_LIST);
        List<String> failWordList = passAndFail.get(FAIL_LIST);
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        
        return wordDAO.insertRandomFailWord(passWordList, failWordList, user);
    }
    
    private Map<String, List<String>> getPassAndFailWordList(String[] answerIds) {
        List<String> passWordList = new ArrayList<String>();
        List<String> failWordList = new ArrayList<String>();
        
        for (int i = 0; i < answerIds.length; i++) {
            if (answerIds[i].endsWith("_1")) {
                passWordList.add(answerIds[i].split("_")[0]);
            } else {
                String id = answerIds[i].split("_")[0];
                
                if (!failWordList.contains(id)) {
                    failWordList.add(id);
                }
            }
        }
        
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put(PASS_LIST, passWordList);
        map.put(FAIL_LIST, failWordList);
        
        return map;
    }
    
    public String insertWord(MultipartFile file) throws IOException {
        StringBuffer result = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;

        String encode = getFileEndcodeUTF8OREUCKR(file);
        
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        
        try {
            isr = new InputStreamReader(file.getInputStream(), encode);
            br = new BufferedReader(isr);

            int i = 0;
            String content;
            while ((content = br.readLine()) != null) {
                String[] word = content.split("/");
                
                wordDAO.insertWord(word, user);
                
                if (i != 0) {
                    result.append(", ");
                }
                
                result.append(word[0]);
                ++i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeBufferReaderAndInputStreamReader(br, isr);
        }
        
        return result.toString();
    }
    
    private String getFileEndcodeUTF8OREUCKR(MultipartFile file) throws IOException {
        String encode = "UTF-8";
        
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            isr = new InputStreamReader(file.getInputStream(), encode);
            br = new BufferedReader(isr);
            
            if (!br.readLine().matches(".*[ㄱ-힣]+.*")) {
                encode = "EUC-KR";
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeBufferReaderAndInputStreamReader(br, isr);
        }
        
        return encode;
    }
    
    private void closeBufferReaderAndInputStreamReader(BufferedReader br, InputStreamReader isr) {
        try {
            if (br != null) {
                br.close();
            }
            
            if (isr != null) {
                isr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
