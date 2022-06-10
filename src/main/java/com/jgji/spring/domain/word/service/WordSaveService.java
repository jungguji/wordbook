package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.word.domain.Row;
import com.jgji.spring.domain.word.domain.Word;
import com.jgji.spring.domain.word.domain.WordDTO.AddWord;
import com.jgji.spring.domain.word.repository.WordRepository;
import com.jgji.spring.global.util.FileUtils;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Service
public class WordSaveService {
    private final static String PASS_LIST = "pass";
    private final static String FAIL_LIST = "fail";
    private final static String WORD_FIELD = "words";
    private final static String MEANING_FIELD = "meanings";

    private final WordRepository wordRepository;

    public void updatePassWord(List<Integer> passIds) {
        List<Word> passWords = this.wordRepository.findByIdIn(passIds);

        for (Word passWord : passWords) {
            passWord.levelUp();
        }
    }

    public List<String> insertFailWord(User user, List<Integer> failIds) {
        List<Word> failWords = this.wordRepository.findByIdIn(failIds);

        final int PLUS_DAY = 1;

        List<Word> newWords = new ArrayList<>();

        for (Word word : failWords) {
            Word newWord = Word.builder()
                    .word(word.getWord())
                    .meaning(word.getMeaning())
                    .nextDate(LocalDate.now().plusDays(PLUS_DAY))
                    .user(user)
                    .build();

            newWords.add(newWord);
        }

        List<Word> words = this.wordRepository.saveAll(newWords);

        return words
                .stream()
                .map(Word::getWord)
                .collect(Collectors.toList());
    }

    public void insertRandomFailWord(User user, String[] answerIds) {
        Map<String, List<Integer>> passAndFail = getPassAndFailWordList(answerIds);
        List<Integer> failIds = passAndFail.get(FAIL_LIST);
        insertFailWord(user, failIds);
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

    public List<String> insertWordByFileUpload(User user, MultipartFile file) {

        final String encode = FileUtils.getFileEncodeUTF8orEucKr(file);

        List<Word> newWordList = new ArrayList<>();
        try (InputStreamReader isr = new InputStreamReader(file.getInputStream(), encode);
            BufferedReader br = new BufferedReader(isr)) {

            List<String[]> wordAndMeanings = getWordAndMeanings(br);

            for (String[] wordAndMeaning : wordAndMeanings) {
                Word insertNewWord = createWord(user, wordAndMeaning[0], wordAndMeaning[1]);
                newWordList.add(insertNewWord);
            }

            this.wordRepository.saveAll(newWordList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newWordList
                .stream()
                .map(Word::getWord)
                .collect(Collectors.toList());
    }

    private List<String[]> getWordAndMeanings(BufferedReader br) throws IOException {

        List<String[]> wordAndMeanings = new ArrayList<>();

        String content;
        while ((content = br.readLine()) != null) {
            String[] wordAndMeaning = content.split("/");

            wordAndMeanings.add(wordAndMeaning);
        }

        return wordAndMeanings;
    }

    public void insertWord(User user, AddWord word) {
        List<Word> newWordList = new ArrayList<>();

        int wordCount = word.getWords().size();
        for (int i = 0; i < wordCount; i++) {
            Word insertNewWord = createWord(user, word.getWords().get(i).getText(), word.getMeanings().get(i).getText());

            newWordList.add(insertNewWord);
        }

        this.wordRepository.saveAll(newWordList);
    }

    private Word createWord(User user, String word, String meaning) {

        return Word.builder()
                .word(word)
                .meaning(meaning)
                .nextDate(LocalDate.now())
                .user(user)
                .build();
    }

    public void updateMeaning(Word word) {
        this.wordRepository.save(word);
    }

    public void delete(String[] rowIds) {
        List<Integer> list = new ArrayList<>();
        for (String rowId : rowIds) {
            list.add(Integer.parseInt(rowId));
        }

        this.wordRepository.deleteByIdIn(list);
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

                sb.append((i + 1));
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
