package com.jgji.spring.domain.word.controller;

import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {WordController.class})
@WithMockUser(username="jgji", password="qwe123", roles="USER")
class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordService service;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new WordController()).build();
    }

    @Test
    void home() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("thymeleaf/index"));
    }

    @Test
    void getToDayWordList() throws Exception {

        //given
        List<Word> list = new ArrayList<Word>();
        Word word0 = new Word();
        word0.setId(1);
        word0.setLevel(3);
        word0.setMeaning("테스트");
        word0.setWord("test");
        word0.setNextDate(LocalDate.of(2020,6,5));

        Word word1 = new Word();
        word1.setId(2);
        word1.setLevel(1);
        word1.setMeaning("사랑");
        word1.setWord("love");
        word1.setNextDate(LocalDate.of(2020,6,5));

        list.add(word0);
        list.add(word1);

        String wordList = "[{\"id\":1,\"word\":\"test\",\"meaning\":\"테스트\",\"nextDate\":\"2020-06-05\",\"level\":3,\"user\":null},{\"id\":2,\"word\":\"love\",\"meaning\":\"사랑\",\"nextDate\":\"2020-06-05\",\"level\":1,\"user\":null}]";

        given(this.service.findToDayWordList()).willReturn(list);

        // when
        final ResultActions action = mockMvc.perform(get("/word/test"))
                .andDo(print());

        //then
        action.andExpect(status().isOk())
            .andExpect(model().attribute("wordList", wordList))
            .andExpect(model().attribute("isExist", true))
            .andExpect(view().name("thymeleaf/word/viewWordTestForm"));
    }
}