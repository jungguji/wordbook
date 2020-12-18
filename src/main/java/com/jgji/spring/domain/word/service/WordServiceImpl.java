package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO;
import com.jgji.spring.domain.word.model.WordDTO.AddWord;
import com.jgji.spring.domain.word.repository.WordRepository;
import org.modelmapper.ModelMapper;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<WordDTO.ResponseWord> findToDayTestWordList() {
        String userId = getUserId();

        List<Word> todayList = repository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now());
        if (todayList.isEmpty()) {
            todayList = repository.findByUserIdOrderByRandom(userId);
        }

        if (todayList.isEmpty()) {
            todayList = repository.findOrderByRandom();
        }

//        List<Word> todayList = Optional.ofNullable(repository.findByUserIdAndNextDateLessThanEqual(userId, LocalDate.now()))
//                .or(() -> Optional.ofNullable(repository.findByUserIdOrderByRandom(userId)))
//                .orElse(repository.findOrderByRandom());

        List<WordDTO.ResponseWord> dtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        int count = 0;
        for (Word word : todayList) {
            if (count == 15) {
                break;
            }
            WordDTO.ResponseWord dto = modelMapper.map(word, WordDTO.ResponseWord.class);
            dtoList.add(dto);
            count++;
        }

        return dtoList;
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
    public void updatePassWord(int[] passIds) {
        List<Word> passList = repository.findByIdIn(Arrays.stream(passIds).boxed().collect(Collectors.toList()));
        User user = userService.getUserByUserName(userService.getCurrentUserName());

        repository.updateSuccessWord(passList, user.getId());
    }

    @Transactional
    public List<String> insertFailWord(int[] failIds) {
        List<Word> failList = repository.findByIdIn(Arrays.stream(failIds).boxed().collect(Collectors.toList()));

        User user = userService.getUserByUserName(userService.getCurrentUserName());
        return repository.insertFailWord(failList, user);
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
        List<Integer> passWordList = new ArrayList<>();
        List<Integer> failWordList = new ArrayList<>();
        
        for (String answerId : answerIds) {
            if (answerId.endsWith("_1")) {
                passWordList.add(Integer.parseInt(answerId.split("_")[0]));
            } else {
                int id = Integer.parseInt(answerId.split("_")[0]);
                
                if (!failWordList.contains(id)) {
                    failWordList.add(id);
                }
            }
        }
        
        Map<String, List<Integer>> map = new HashMap<>();
        map.put(PASS_LIST, passWordList);
        map.put(FAIL_LIST, failWordList);
        
        return map;
    }
    
    @Transactional
    public String insertWordByFileUpload(MultipartFile file) {
        StringBuilder result = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader br = null;

        String encode = getFileEncodeUTF8OREUCKR(file);

        try {
            isr = new InputStreamReader(file.getInputStream(), encode);
            br = new BufferedReader(isr);

            List<Word> newWordList = new ArrayList<>();
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
        List<Word> newWordList = new ArrayList<>();

        int wordCount = word.getWords().size();
        for (int i = 0; i < wordCount; i++) {
            Word insertNewWord = setWordAttribute(word.getWords().get(i).getText(), word.getMeanings().get(i).getText());
            newWordList.add(insertNewWord);
        }

        repository.saveAll(newWordList);

        return "good";
    }
    
    private String getFileEncodeUTF8OREUCKR(MultipartFile file) {
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

        return  Word.builder()
                .word(word)
                .meaning(meaning)
                .nextDate(LocalDate.now())
                .user(user)
                .build();
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
        List<Integer> list = new ArrayList<>();
        for (String rowId : rowIds) {
            list.add(Integer.parseInt(rowId));
        }
        
        repository.deleteByIdIn(list);
    }
    
    public List<Map<String, Object>> getFrequentFailWord(String userId) {
        return repository.findFrequentFailWord(userId);
    }
    
    public BindingResult getCreateWordBindingResult(AddWord word, BindingResult bindingResult) {
        
        String wordRowErrorNumber = getErrorRowNumber(word.getWords());
        String meaningRowErrorNumber = getErrorRowNumber(word.getMeanings());

        BindingResult result = setRejectValue(bindingResult, wordRowErrorNumber, WORD_FIELD, "단어가");
        result = setRejectValue(result, meaningRowErrorNumber, MEANING_FIELD, "뜻이");

        return result;
    }
    
    private String getErrorRowNumber(List<Row> rows) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows.size(); i++) {
            if (StringUtils.isEmpty(rows.get(i).getText())) {

                if (sb.length() != 0) {
                    sb.append(", ");
                }

                sb.append((i+1));
            }
        }

        return sb.toString();
    }

    private BindingResult setRejectValue(BindingResult bindingResult, String errorMsg, String fieldName, String value) {
        if (!StringUtils.isEmpty(errorMsg)) {
            StringBuilder errorMessage = new StringBuilder(errorMsg)
            .append("행에 " + value + " 비어 있습니다.");

            bindingResult.rejectValue(fieldName, fieldName, errorMessage.toString());
        }
        
        return bindingResult;
    }
}
