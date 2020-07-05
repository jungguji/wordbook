package com.jgji.spring.domain.word.repository;

import com.jgji.spring.domain.user.model.User;
import com.jgji.spring.domain.user.repository.UserRepository;
import com.jgji.spring.domain.word.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
class WordRepositoryTest {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String USER_ID = "1";
    private static User user;
    private static User user1;

    private static Word word;
    private static Word word1;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("jgji");
        user.setPassword("qwe123");

        userRepository.save(this.user);

        user1 = new User();
        user.setUsername("haha");
        user.setPassword("qwe123");

        userRepository.save(this.user1);

        List<Word> givenWordList = getWordGiven();
        this.word = givenWordList.get(0);
        this.word1 = givenWordList.get(1);
    }

    @Test
    void findByUserId() {
        //given
        //when
        List<Word> list = wordRepository.findByUserId(this.word.getUser().getId());

        //than
        assertThat(list).contains(word);
    }

    @Test
    void findByUserIdAndNextDateLessThanEqual() {
        //given
        LocalDate local = LocalDate.now();
        local = local.plusDays(5);
        this.word1.setNextDate(local);

        wordRepository.save(word);
        wordRepository.save(word1);

        //when
        List<Word> list = wordRepository.findByUserIdAndNextDateLessThanEqual(this.word.getUser().getId(), LocalDate.now().plusDays(5));

        //than
        assertThat(list).contains(word);
        assertThat(list).contains(word1);
    }

    @Test
    void findOrderByRandom() {
        //given
        //when
        List<Word> list = wordRepository.findOrderByRandom();

        //than
        assertThat(list).contains(word);
        assertThat(list).contains(word1);
    }

    @Test
    void findByUserIdOrderByRandom() {
        //given
        //when
        List<Word> list = wordRepository.findByUserIdOrderByRandom(this.word.getUser().getId());

        //than
        assertThat(list).contains(word);
        assertThat(list).contains(word1);
    }

    @Test
    void findByIdIn() {
        //given
        Word word2 = new Word();
        word2.setLevel(1);
        word2.setWord("gaga");
        word2.setMeaning("gaga");
        word2.setUser(user);

        wordRepository.save(word2);

        List<Integer> createWordIdList = Arrays.asList(
                word1.getId(), word2.getId()
        );

        //when
        List<Word> list = wordRepository.findByIdIn(createWordIdList);

        //than
        assertThat(list).isNotIn(word);
        assertThat(list).contains(word1);
        assertThat(list).contains(word2);
    }

    @Test
    void deleteByIdIn() {
        //given
        List<Integer> ids = Arrays.asList( new Integer(1));

        //when
        wordRepository.deleteByIdIn(ids);

        List<Word> list = wordRepository.findByUserId(USER_ID);

        //than
        assertThat(list).contains(word1)
                .isNotIn(word);
    }

    private List<Word> getWordGiven() {
        Word word = new Word();
        word.setLevel(1);
        word.setWord("test");
        word.setMeaning("확인");
        word.setNextDate(LocalDate.now());
        word.setUser(user);

        Word word1 = new Word();
        word1.setLevel(1);
        word1.setWord("test2");
        word1.setMeaning("확인2");
        word1.setUser(user);

        Word answerWord = wordRepository.save(word);
        Word answerWord1 = wordRepository.save(word1);

        return Arrays.asList(answerWord, answerWord1);
    }
}