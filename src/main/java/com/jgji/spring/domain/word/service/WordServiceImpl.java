package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO.AddWord;
import com.jgji.spring.domain.word.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wordService")
public class WordServiceImpl implements WordService{
    private final static String PASS_LIST = "pass";
    private final static String FAIL_LIST = "fail";
    private final static String WORD_FIELD = "words";
    private final static String MEANING_FIELD = "meanings";
    private final static String ENCODE_UTF8 = "UTF-8";
    private final static String ENCODE_EUCKR = "EUC-KR";

    private final WordRepository repository;
    
    private final UserService userService;

    public WordServiceImpl(WordRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<Word> findAllByUserId() {
        String userId = getUserId();
        return repository.findByUserId(userId);
    }
    
    public List<Word> findToDayWordList() {
        String userId = getUserId();
        return repository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now());
    }
    
    public List<Word> getRandomWordList() {
        String userId = getUserId();
        return repository.findByUserIdOrderByRandom(userId);
    }

    private String getUserId() {
        return this.userService.getUserIdByLoginUserName();
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
        StringBuilder result = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader br = null;

        String encode = getFileEndcodeUTF8OREUCKR(file);

        try {
            isr = new InputStreamReader(file.getInputStream(), encode);
            br = new BufferedReader(isr);

            List<Word> newWordList = new ArrayList<Word>();
            int i = 0;
            String content;

            while ((content = br.readLine()) != null) {
                String[] wordAndMeaning = content.split("/");
                
                if (i != 0) {
                    result.append(", ");
                }
                
                result.append(wordAndMeaning[0]);
                ++i;

                Word insertNewWord = setWordAttribute(wordAndMeaning[0], wordAndMeaning[1]);
                newWordList.add(insertNewWord);
            }

            repository.saveAll(newWordList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeBufferReaderAndInputStreamReader(br, isr);
        }
        
        return result.toString();
    }
    
    @Transactional
    public String insertWord(AddWord word) {
        List<Word> newWordList = new ArrayList<Word>();

        int wordCount = word.getWords().size();
        for (int i = 0; i < wordCount; i++) {
            Word insertNewWord = setWordAttribute(word.getWords().get(i).getText(), word.getMeanings().get(i).getText());
            newWordList.add(insertNewWord);
        }

        repository.saveAll(newWordList);

        return "good";
    }
    
    private String getFileEndcodeUTF8OREUCKR(MultipartFile file) throws IOException {
        String encode = ENCODE_UTF8;
        
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            isr = new InputStreamReader(file.getInputStream(), encode);
            br = new BufferedReader(isr);
            
            if (!br.readLine().matches(".*[ㄱ-힣]+.*")) {
                encode = ENCODE_EUCKR;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeBufferReaderAndInputStreamReader(br, isr);
        }
        
        return encode;
    }
    
    private Word setWordAttribute(String word, String meaning) {
        User user = userService.getUserByUserName(userService.getCurrentUserName());

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

    @Transactional
    public void delete(String[] rowIds) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0 ; i < rowIds.length; i++) {
            list.add(Integer.parseInt(rowIds[i]));
        }
        
        repository.deleteByIdIn(list);
    }
    
    public List<Map<String, Object>> getFrequentFailWord(String userId) {
        return repository.findFrequentFailWord(userId);
    }
    
    public BindingResult getCreateWordBindingResult(AddWord word, BindingResult bindingResult) {
        
        String wordRowErrorMsg = getRejectMessage(word, bindingResult, WORD_FIELD);
        String meaningRowErrorMsg = getRejectMessage(word, bindingResult, MEANING_FIELD);

        BindingResult result = setRejectValue(bindingResult, wordRowErrorMsg, WORD_FIELD);
        result = setRejectValue(result, meaningRowErrorMsg, MEANING_FIELD);

        return result;
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
            StringBuilder errorMessage = new StringBuilder(errorMsg);

            if (WORD_FIELD.equals(fieldName)) {
                errorMessage.append("행에 단어가 비어 있습니다.");
            } else {
                errorMessage.append("행에 뜻이 비어 있습니다.");
            }
            
            bindingResult.rejectValue(fieldName, fieldName, errorMessage.toString());
        }
        
        return bindingResult;
    }
}
