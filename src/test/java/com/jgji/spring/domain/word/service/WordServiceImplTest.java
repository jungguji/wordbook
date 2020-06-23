package com.jgji.spring.domain.word.service;

import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

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

        list = new ArrayList<Word>();
        Word word0 = new Word();
        word0.setId(1);
        word0.setLevel(3);
        word0.setMeaning("테스트");
        word0.setWord("test");
        word0.setNextDate(LocalDate.of(2020, 6, 5));

        Word word1 = new Word();
        word1.setId(2);
        word1.setLevel(1);
        word1.setMeaning("사랑");
        word1.setWord("love");
        word1.setNextDate(LocalDate.of(2020, 6, 5));

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
        List<Integer> failWordList = Arrays.asList(1);

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
}