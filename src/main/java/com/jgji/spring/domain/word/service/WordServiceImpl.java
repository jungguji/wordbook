package com.jgji.spring.domain.word.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.repository.WordMapper;

@Service("wordService")
public class WordServiceImpl implements WordService{
    
    final static String PASS_LIST = "pass";
    final static String FAIL_LIST = "fail";
    final static String WORD_FIELD = "words";
    final static String MEANING_FIELD = "meanings";
    
    @Autowired
    private WordMapper wordMapper;
    
    @Autowired
    private UserService userService;
    
    public List<Word> findAllByUserId() {
//        String userId = userService.getUserIdByUserName();
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("userId", "3");
        
        List<String> wordIdList = new ArrayList<String>();
        wordIdList.add("303");
        wordIdList.add("300");
        wordIdList.add("301");
        
        map.put("wordIdList", wordIdList);
        
        return wordMapper.findWordListByUserIdAndWordId(map);
    }
    
    public List<Word> getToDayWordList(Word word) {
        String userId = getUserId();
        return wordMapper.findToDayWordListByUserId(userId, word.getNextDate());
    }
    
    public List<Word> getRandomWordList() {
        String userId = getUserId();
        return wordMapper.findRandomWordListByUserId(userId);
    }
    
    public List<Word> getRandomByAllWordList() {
        return wordMapper.getRandomByAllWordList();
    }
    
    private String getUserId() {
        return userService.getUserIdByUserName(userService.getCurrentUserName());
    }

    public String updateNextDateAndInsert(String[] answerIds) {
        Map<String, List<String>> passAndFail = getPassAndFailWordList(answerIds);
        List<String> passWordList = passAndFail.get(PASS_LIST);
        List<String> failWordList = passAndFail.get(FAIL_LIST);
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        String userId = user.getId();
        
        updateSuccessWord(passWordList, userId);
        insertFailWord(failWordList, userId);
        
        return "OK";
    }
    
    public String insertRandomFailWord(String[] answerIds) {
        Map<String, List<String>> passAndFail = getPassAndFailWordList(answerIds);
        List<String> failWordList = passAndFail.get(FAIL_LIST);
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        String userId = user.getId();
        
        insertFailWord(failWordList, userId);
        
        return "OK";
    }
    
    private void updateSuccessWord(List<String> passWordList, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("wordIdList", passWordList);
        
        System.out.println(passWordList.toString());
        
        List<Word> list = wordMapper.findWordListByUserIdAndWordId(map);
        
        System.out.println(list.toString());
        LocalDate nextDate = LocalDate.now();
        for (Word word : list) {
            int currentLevel = word.getLevel();
            
            word.setNextDate(nextDate.plusDays(addDate(currentLevel)));
            word.setLevel(++currentLevel);
            
            System.out.println(word.toString());
            wordMapper.updateSuccessWord(word);
        }
    }
    
    private void insertFailWord(List<String> failWordList, String userId) {
        final int PLUS_DAY = 1;
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("wordIdList", failWordList);
        
        List<Word> list = wordMapper.findWordListByUserIdAndWordId(map);
        
        LocalDate nextDate = LocalDate.now().plusDays(PLUS_DAY);
        
        for (Word word : list) {
            Word insertNewWord = new Word();
            insertNewWord.setWord(word.getWord());
            insertNewWord.setMeaning(word.getMeaning());
            insertNewWord.setNextDate(nextDate);
            insertNewWord.setUsersId(userId);
            
            System.out.println(insertNewWord.toString());
            
            wordMapper.insertFailWord(insertNewWord);
        }
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
//    
//    public String insertWord(MultipartFile file) throws IOException {
//        StringBuffer result = new StringBuffer();
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//
//        String encode = getFileEndcodeUTF8OREUCKR(file);
//        
//        User user = userService.getUserByUserName(userService.getCurrentUserName());
//        
//        try {
//            isr = new InputStreamReader(file.getInputStream(), encode);
//            br = new BufferedReader(isr);
//
//            int i = 0;
//            String content;
//            while ((content = br.readLine()) != null) {
//                String[] wordAndMeaning = content.split("/");
//                
//                Word insertNewWord = setWordAttribute(wordAndMeaning[0], wordAndMeaning[1], user);
//                
//                wordMapper.insertWord(insertNewWord);
//                
//                if (i != 0) {
//                    result.append(", ");
//                }
//                
//                result.append(wordAndMeaning[0]);
//                ++i;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            closeBufferReaderAndInputStreamReader(br, isr);
//        }
//        
//        return result.toString();
//    }
//    
//    public String insertWord(Word word) {
//        User user = userService.getUserByUserName(userService.getCurrentUserName());
//        
//        word.setUser(user);
//        int wordCount = word.getWords().size();
//        for (int i = 0; i < wordCount; i++) {
//            Word insertNewWord = setWordAttribute(word.getWords().get(i).getText(), word.getMeanings().get(i).getText(), user);
//            
//            wordMapper.insertWord(insertNewWord);
//        }
//        
//        return "good";
//    }
//    
//    private String getFileEndcodeUTF8OREUCKR(MultipartFile file) throws IOException {
//        String encode = "UTF-8";
//        
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//
//        try {
//            isr = new InputStreamReader(file.getInputStream(), encode);
//            br = new BufferedReader(isr);
//            
//            if (!br.readLine().matches(".*[ㄱ-힣]+.*")) {
//                encode = "EUC-KR";
//            }
//            
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            closeBufferReaderAndInputStreamReader(br, isr);
//        }
//        
//        return encode;
//    }
//    
//    private Word setWordAttribute(String word, String meaning, User user) {
//        Word insertNewWord = new Word();
//        insertNewWord.setWord(word);
//        insertNewWord.setMeaning(meaning);
//        insertNewWord.setNextDate(LocalDate.now());
//        insertNewWord.setUser(user);
//        
//        return insertNewWord;
//    }
//    
//    private void closeBufferReaderAndInputStreamReader(BufferedReader br, InputStreamReader isr) {
//        try {
//            if (br != null) {
//                br.close();
//            }
//            
//            if (isr != null) {
//                isr.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    
//    public boolean updateMeaning(Word word) {
//        wordMapper.updateMeaning(word);
//        return true;
//    }
//    
//    public void delete(String[] rowIds) {
//        wordMapper.delete(rowIds);
//    }
//    
//    public List<Map<String, Object>> getFrequentFailWord(String userId) {
//        List<Map<String, Object>> test = wordMapper.getFrequentFailWord(userId);
//        
//        return test;
//    }
//    
//    public BindingResult getCreateWordBindingResult(Word word, BindingResult bindingResult) {
//        
//        String wordRowErrorMsg = getRejectMessage(word, bindingResult, WORD_FIELD);
//        
//        System.out.println("wordRowErrorMsg >> " + wordRowErrorMsg);
//        String meaingRowErrorMsg = getRejectMessage(word, bindingResult, MEANING_FIELD);
//        
//        System.out.println("meaingRowErrorMsg >> " + meaingRowErrorMsg);
//        
//        bindingResult = setRejectValue(bindingResult, wordRowErrorMsg, WORD_FIELD);
//        bindingResult = setRejectValue(bindingResult, meaingRowErrorMsg, MEANING_FIELD);
//        
//        return bindingResult;
//    }
//    
//    private String getRejectMessage(Word word, BindingResult bindingResult, String fieldName) {
//        StringBuilder sb = new StringBuilder();
//        
//        int wordCount = word.getWords().size();
//        
//        List<Row> row;
//        if (WORD_FIELD.equals(fieldName)) {
//            row = word.getWords();
//        } else {
//            row = word.getMeanings();
//        }
//        
//        for (int i = 0; i < wordCount; i++) {
//            int wordsErrorCount = bindingResult.getFieldErrorCount(fieldName);
//            if (StringUtils.isEmpty(row.get(i).getText()) && wordsErrorCount == 0) {
//                
//                if (sb.length() != 0) {
//                    sb.append(", ");
//                }
//                sb.append((i+1));
//            }
//        }
//        
//        return sb.toString();
//    }
//    
//    private BindingResult setRejectValue(BindingResult bindingResult, String errorMsg, String fieldName) {
//        if (!StringUtils.isEmpty(errorMsg)) {
//            if (WORD_FIELD.equals(fieldName)) {
//                errorMsg += "행에 단어가 비어 있습니다.";
//            } else {
//                errorMsg += "행에 뜻이 비어 있습니다.";
//            }
//            
//            
//            System.out.println("fieldName >> " + fieldName + " error >> " + errorMsg);
//            bindingResult.rejectValue(fieldName, fieldName, errorMsg);
//        }
//        
//        return bindingResult;
//    }
}
