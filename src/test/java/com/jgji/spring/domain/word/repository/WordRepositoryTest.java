package com.jgji.spring.domain.word.repository;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.word.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WordRepositoryTest {

    @Autowired
    private WordRepository wordRepository;

    @Autowired private DataSource dataSource;

    private static final String USER_ID = "1";
    private static User user;
    private static User user1;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("jgji");
        user.setId(USER_ID);
        user.setPassword("qwe123");

        user1 = new User();
        user1.setId("2");
    }

    @Test
    void test() {
        assertThat(dataSource).isNotNull();
    }

    @Test
    void findByUserId() {
        //given
        Word word = new Word();
        word.setId(100);
        word.setLevel(1);
        word.setWord("test");
        word.setMeaning("확인");
        word.setNextDate(LocalDate.now());
        word.setUser(user);

        wordRepository.save(word);

        //when
        List<Word> list = wordRepository.findByUserId(USER_ID);

        //than
        assertThat(list).extracting("word", String.class)
                .contains("test");
        assertThat(list).extracting("meaning", String.class)
                .contains("확인");
    }

    @Test
    void findByUserIdAndNextDateLessThanEqual() {
        //given
        Word word = new Word();
        word.setId(2324);
        word.setLevel(1);
        word.setWord("tttt");
        word.setMeaning("확인");
        word.setNextDate(LocalDate.now());
        word.setUser(user);

        Word word1 = new Word();
        word1.setId(232);
        word1.setLevel(1);
        word1.setWord("하하호호");
        word1.setMeaning("헤헤");
        word1.setUser(user);
        LocalDate local = LocalDate.now();
        local = local.plusDays(5);

        word1.setNextDate(local);

        wordRepository.save(word);
        wordRepository.save(word1);

        //when
        List<Word> list = wordRepository.findByUserIdAndNextDateLessThanEqual(USER_ID, LocalDate.now().plusDays(5));

        //than
        assertThat(list).extracting("word", String.class)
                .contains("tttt", "하하호호");
        assertThat(list).extracting("meaning", String.class)
                .contains("확인", "헤헤");
    }

    @Test
    void findOrderByRandom() {
        //given
        Word word = new Word();
        word.setId(100);
        word.setLevel(1);
        word.setWord("test");
        word.setMeaning("확인");
        word.setNextDate(LocalDate.now());
        word.setUser(user);

        Word word1 = new Word();
        word1.setId(102);
        word1.setLevel(1);
        word1.setWord("다른사람");
        word1.setMeaning("것입니다");
        word1.setUser(user1);

        wordRepository.save(word);
        wordRepository.save(word1);

        //when
        List<Word> list = wordRepository.findOrderByRandom();

        //than
        assertThat(list).extracting("word", String.class)
                .contains("test", "다른사람");
        assertThat(list).extracting("meaning", String.class)
                .contains("확인", "것입니다");
    }

    @Test
    void findByUserIdOrderByRandom() {
        //given
        Word word = new Word();
        word.setId(84);
        word.setLevel(1);
        word.setWord("test");
        word.setMeaning("확인");
        word.setNextDate(LocalDate.now());
        word.setUser(user);

        Word word1 = new Word();
        word1.setId(85);
        word1.setLevel(1);
        word1.setWord("같은사람");
        word1.setMeaning("것입니다");
        word1.setUser(user);

        wordRepository.save(word);
        wordRepository.save(word1);

        //when
        List<Word> list = wordRepository.findByUserIdOrderByRandom(USER_ID);

        //than
        assertThat(list).extracting("word", String.class)
                .contains("test", "같은사람");
        assertThat(list).extracting("meaning", String.class)
                .contains("확인", "것입니다");

    }

    @Test
    void findByIdIn() {
        //given
        Word word = new Word();
        word.setId(5);
        word.setLevel(1);
        word.setWord("test");
        word.setMeaning("확인");
        word.setNextDate(LocalDate.now());
        word.setUser(user);

        Word word1 = new Word();
        word1.setId(6);
        word1.setLevel(1);
        word1.setWord("같은사람");
        word1.setMeaning("것입니다");
        word1.setUser(user);

        List<Integer> createWordIdList = Arrays.asList(
                5,6
        );

        //when
        List<Word> list = wordRepository.findByIdIn(createWordIdList);

        //than
        assertThat(list).extracting("word", String.class)
                .contains("like", "apple");
        assertThat(list).extracting("meaning", String.class)
                .contains("같은, 좋은, 와 비슷한", "사과,대도시");
    }

    @Test
    void deleteByIdIn() {
    }
}