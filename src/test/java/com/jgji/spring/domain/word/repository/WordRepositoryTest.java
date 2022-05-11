package com.jgji.spring.domain.word.repository;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.user.repository.UserRepository;
import com.jgji.spring.domain.word.domain.Word;
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
        user = User.builder()
                .username("jgji")
                .password("qwe123")
                .build();

        userRepository.save(this.user);

        user1 = User.builder()
                .username("haha")
                .password("qwe123")
                .build();

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
        this.word1.updateDate(local);

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
        Word word2 = Word.builder()
                .word("gaga")
                .meaning("gaga")
                .level(1)
                .user(user)
                .build();

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
        List<Integer> ids = Arrays.asList( new Integer(word.getId()));

        //when
        wordRepository.deleteByIdIn(ids);

        List<Word> list = wordRepository.findByUserId(user.getId());

        //than
        assertThat(list).contains(word1)
                .isNotIn(word);
    }

    private List<Word> getWordGiven() {
        Word word = Word.builder()
                .word("test")
                .meaning("확인")
                .level(1)
                .nextDate(LocalDate.now())
                .user(user)
                .build();

        Word word1 =Word.builder()
                .word("test2")
                .meaning("확인2")
                .level(1)
                .nextDate(LocalDate.now())
                .user(user)
                .build();

        Word answerWord = wordRepository.save(word);
        Word answerWord1 = wordRepository.save(word1);

        return Arrays.asList(answerWord, answerWord1);
    }
}