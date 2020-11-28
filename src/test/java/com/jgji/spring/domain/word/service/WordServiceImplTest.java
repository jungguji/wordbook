package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Row;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO;
import com.jgji.spring.domain.word.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class WordServiceImplTest {

    @MockBean
    private WordRepository repository;

    @MockBean
    private UserService userService;

    private WordService wordService;

    private List<Word> list;

    @BeforeEach
    public void setup() {

        list = new ArrayList<>();
        Word word0 = Word.builder()
                .word("test")
                .meaning("테스트")
                .level(3)
                .nextDate(LocalDate.of(2020, 6, 5))
                .build();

        Word word1 = Word.builder()
                .word("love")
                .meaning("사랑")
                .level(1)
                .nextDate(LocalDate.of(2020, 6, 5))
                .build();

        list.add(word0);
        list.add(word1);

        wordService = new WordServiceImpl(repository, userService);
    }

    @Test
    void findAllByUserId() {
        //gvien
        given(this.repository.findByUserId(any())).willReturn(list);

        //when
        List<Word> wordList = wordService.findAllByUserId();

        //than
        assertThat(wordList).extracting("word", String.class)
                .contains("test","love");
        assertThat(wordList).extracting("meaning", String.class)
                .contains("테스트","사랑");
    }

    @Test
    void findToDayWordList() {
        //given
        given(this.repository.findByUserIdAndNextDateLessThanEqual(any(), any(LocalDate.class))).willReturn(list);

        //when
        List<Word> wordList = wordService.findToDayWordList();

        //than
        assertThat(wordList).extracting("word", String.class)
                .contains("test","love");
        assertThat(wordList).extracting("meaning", String.class)
                .contains("테스트","사랑");
    }

    @Test
    void getRandomWordList() {
        //given
        given(this.repository.findByUserIdOrderByRandom(any())).willReturn(list);

        //when
        List<Word> wordList = wordService.getRandomWordList();

        //than
        assertThat(wordList).extracting("word", String.class)
                .contains("test","love");
        assertThat(wordList).extracting("meaning", String.class)
                .contains("테스트","사랑");
    }

    @Test
    void getRandomByAllWordList() {
        //given
        given(this.repository.findOrderByRandom()).willReturn(list);

        //when
        List<Word> wordList = wordService.getRandomByAllWordList();

        //than
        assertThat(wordList).extracting("word", String.class)
                .contains("test","love");
        assertThat(wordList).extracting("meaning", String.class)
                .contains("테스트","사랑");
    }

    @Test
    void getPassAndFailWordList_fail에_다른_값이_들어가서_실패()  throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        String[] answerIds = new String[] {"6_1", "4_1", "5_1", "9_3"};
        WordService w = new WordServiceImpl(repository, userService);

        Method method = w.getClass().getDeclaredMethod("getPassAndFailWordList", String[].class);
        method.setAccessible(true);

        List<Integer> passWordList = Arrays.asList(6,4,5);
        List<Integer> failWordList = Arrays.asList(9); // 이걸 실패하게 만들었음

        Object[] obj = new Object[] {answerIds};

        //when
        Map<String, List<Integer>> map = (Map<String, List<Integer>>) method.invoke(w, obj);

        //than
        assertThat(map).extracting("pass", String.class)
                .contains(passWordList);
        assertThat(map).extracting("fail", String.class)
                .contains(failWordList);
    }

    @Test
    void getPassAndFailWordList_성공()  throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        String[] answerIds = new String[] {"6_1", "4_1", "5_1", "9_3"};
        WordService w = new WordServiceImpl(repository, userService);

        Method method = w.getClass().getDeclaredMethod("getPassAndFailWordList", String[].class);
        method.setAccessible(true);

        List<Integer> passWordList = Arrays.asList(6,4,5);
        List<Integer> failWordList = Arrays.asList(9);

        Object[] obj = new Object[] {answerIds};

        //when
        Map<String, List<Integer>> map = (Map<String, List<Integer>>) method.invoke(w, obj);

        //than
        assertThat(map).extracting("pass", String.class)
                .contains(passWordList);
        assertThat(map).extracting("fail", String.class)
                .contains(failWordList);
    }

    @Test
    void getFileEncodeUTF8OREUCKR_성공() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // given
        String fileDir = "src/test/resources/";
        String fileName = "ttest.txt";
        String fileFullPath = fileDir + File.separator + fileName;

        MockMultipartFile mockFile = new MockMultipartFile(fileName, new FileInputStream(new File(fileFullPath)));

        WordService w = new WordServiceImpl(repository, userService);

        Method method = w.getClass().getDeclaredMethod("getFileEncodeUTF8OREUCKR", MultipartFile.class);
        method.setAccessible(true);

        Object[] obj = new Object[] {mockFile};

        //when
        String encode = (String) method.invoke(w, obj);

        //than
        assertEquals("UTF-8", encode);
    }

    @Test
    void getErrorRowNumber_성공() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        WordService w = new WordServiceImpl(repository, userService);
        Method method = w.getClass().getDeclaredMethod("getErrorRowNumber", List.class);
        method.setAccessible(true);

        Row row1 = new Row();
        row1.setText("");
        Row row2 = new Row();
        row2.setText("");

        List<Row> rows = Arrays.asList(
                row1, row2
        );

        Object[] obj = new Object[] {rows};

        //when
        String str = (String) method.invoke(w, obj);

        //than
        assertEquals("1, 2", str);
    }

    @Test
    void getErrorRowNumbesetRejectValue_성공() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        WordService w = new WordServiceImpl(repository, userService);
        Method method = w.getClass().getDeclaredMethod("setRejectValue", BindingResult.class, String.class, String.class, String.class);
        method.setAccessible(true);

        BindingResult bindingResult = new MapBindingResult(new HashMap(), "");

        Object[] obj = new Object[] {bindingResult, new String("1"), new String("word"), new String("단어가")};

        //when
        BindingResult bresult = (BindingResult) method.invoke(w, obj);

        //than
        assertTrue(bresult.hasErrors());
        assertEquals(1, bresult.getErrorCount());
        assertEquals("1행에 단어가 비어 있습니다.", bresult.getFieldError("word").getDefaultMessage());
    }

    @Test
    void insertWordByFileUpload() throws IOException, NoSuchMethodException {
        // given
        String fileDir = "src/test/resources/";
        String fileName = "ttest.txt";
        String fileFullPath = fileDir + File.separator + fileName;

        MockMultipartFile mockFile = new MockMultipartFile(fileName, new FileInputStream(new File(fileFullPath)));

        // when
        String result = wordService.insertWordByFileUpload(mockFile);

        // than
        assertEquals("I take off my hat, The dog walks near the horse", result);
    }
}