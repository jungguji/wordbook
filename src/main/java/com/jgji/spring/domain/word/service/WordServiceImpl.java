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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO.AddWord;
import com.jgji.spring.domain.word.repository.WordRepository;

@Service("wordService")
public class WordServiceImpl implements WordService{
    final static String PASS_LIST = "pass";
    final static String FAIL_LIST = "fail";
    final static String WORD_FIELD = "words";
    final static String MEANING_FIELD = "meanings";
    
    @Autowired
    private WordRepository repository;
    
    @Autowired
    private UserService userService;
    
    public List<Word> findAllByUserId() {
        String userId = userService.getUserIdByUserName();
        return repository.findByUserId(userId);
    }
    
    public List<Word> findToDayWordList(Word word) {
        String userId = userService.getUserIdByUserName();
        return repository.findByUserIdAndNextDateLessThanEqual(userId, word.getNextDate());
    }
    
    public List<Word> getRandomWordList() {
        String userId = userService.getUserIdByUserName();
        return repository.findByUserIdOrderByRandom(userId);
    }
    
    public List<Word> getRandomByAllWordList() {
        return repository.findOrderByRandom();
    }
    
    @Transactional
    public boolean updateNextDateAndInsert(String[] answerIds) {
        Map<String, List<Integer>> passAndFail = getPassAndFailWordList(answerIds);
        List<Integer> passWordList = passAndFail.get(PASS_LIST);
        List<Integer> failWordList = passAndFail.get(FAIL_LIST);
        
        List<Word> passList = repository.findByIdIn(passWordList);
        List<Word> failList = repository.findByIdIn(failWordList);
        
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        
        repository.updateSuccessWord(passList, user.getId());
        repository.insertFailWord(failList, user);
        
        return true;
    }
    
    @Transactional
    public boolean insertRandomFailWord(String[] answerIds) {
        Map<String, List<Integer>> passAndFail = getPassAndFailWordList(answerIds);
        List<Integer> failWordList = passAndFail.get(FAIL_LIST);
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        
        List<Word> wordList = repository.findByIdIn(failWordList);
        
        repository.insertFailWord(wordList, user);
        
        return true;
    }
    
    private Map<String, List<Integer>> getPassAndFailWordList(String[] answerIds) {
        List<Integer> passWordList = new ArrayList<Integer>();
        List<Integer> failWordList = new ArrayList<Integer>();
        
        for (int i = 0; i < answerIds.length; i++) {
            if (answerIds[i].endsWith("_1")) {
                passWordList.add(Integer.parseInt(answerIds[i].split("_")[0]));
            } else {
                int id = Integer.parseInt(answerIds[i].split("_")[0]);
                
                if (!failWordList.contains(id)) {
                    failWordList.add(id);
                }
            }
        }
        
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        map.put(PASS_LIST, passWordList);
        map.put(FAIL_LIST, failWordList);
        
        return map;
    }
    
    @Transactional
    public String insertWordByFileUpload(MultipartFile file) throws IOException {
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
                String[] wordAndMeaning = content.split("/");
                
                Word insertNewWord = setWordAttribute(wordAndMeaning[0], wordAndMeaning[1], user);
                
                repository.save(insertNewWord);
                
                if (i != 0) {
                    result.append(", ");
                }
                
                result.append(wordAndMeaning[0]);
                ++i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeBufferReaderAndInputStreamReader(br, isr);
        }
        
        return result.toString();
    }
    
    @Transactional
    public String insertWord(AddWord word) {
        User user = userService.getUserByUserName(userService.getCurrentUserName());
        
        int wordCount = word.getWords().size();
        for (int i = 0; i < wordCount; i++) {
            Word insertNewWord = setWordAttribute(word.getWords().get(i).getText(), word.getMeanings().get(i).getText(), user);
            
            repository.save(insertNewWord);
        }
        
        return "good";
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
    
    private Word setWordAttribute(String word, String meaning, User user) {
        Word insertNewWord = new Word();
        insertNewWord.setWord(word);
        insertNewWord.setMeaning(meaning);
        insertNewWord.setNextDate(LocalDate.now());
        insertNewWord.setUser(user);
        
        return insertNewWord;
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
    
    public boolean updateMeaning(Word word) {
        repository.save(word);
        return true;
    }
    
    public void delete(String[] rowIds) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0 ; i < rowIds.length; i++) {
            list.add(Integer.parseInt(rowIds[i]));
        }
        
        repository.deleteByIdIn(list);
    }
    
    public List<Map<String, Object>> getFrequentFailWord(String userId) {
        List<Map<String, Object>> test = repository.findFrequentFailWord(userId);
        
        return test;
    }
    
    public BindingResult getCreateWordBindingResult(AddWord word, BindingResult bindingResult) {
        
        String wordRowErrorMsg = getRejectMessage(word, bindingResult, WORD_FIELD);
        String meaingRowErrorMsg = getRejectMessage(word, bindingResult, MEANING_FIELD);
        
        bindingResult = setRejectValue(bindingResult, wordRowErrorMsg, WORD_FIELD);
        bindingResult = setRejectValue(bindingResult, meaingRowErrorMsg, MEANING_FIELD);
        
        return bindingResult;
    }
    
    private String getRejectMessage(AddWord word, BindingResult bindingResult, String fieldName) {
        StringBuilder sb = new StringBuilder();
        
        int wordCount = word.getWords().size();
        
        List<Row> row;
        if (WORD_FIELD.equals(fieldName)) {
            row = word.getWords();
        } else {
            row = word.getMeanings();
        }
        
        for (int i = 0; i < wordCount; i++) {
            int wordsErrorCount = bindingResult.getFieldErrorCount(fieldName);
            if (StringUtils.isEmpty(row.get(i).getText()) && wordsErrorCount == 0) {
                
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append((i+1));
            }
        }
        
        return sb.toString();
    }
    
    private BindingResult setRejectValue(BindingResult bindingResult, String errorMsg, String fieldName) {
        if (!StringUtils.isEmpty(errorMsg)) {
            if (WORD_FIELD.equals(fieldName)) {
                errorMsg += "행에 단어가 비어 있습니다.";
            } else {
                errorMsg += "행에 뜻이 비어 있습니다.";
            }
            
            bindingResult.rejectValue(fieldName, fieldName, errorMsg);
        }
        
        return bindingResult;
    }
}
