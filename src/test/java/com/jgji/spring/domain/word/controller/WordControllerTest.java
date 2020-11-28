package com.jgji.spring.domain.word.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgji.spring.domain.user.service.UserService;
import com.jgji.spring.domain.word.model.Word;
import com.jgji.spring.domain.word.model.WordDTO;
import com.jgji.spring.domain.word.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private List<Word> list;

    @BeforeEach
    public void setUp() {
        list = new ArrayList<Word>();
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
        String wordList = getWordList();

        given(this.service.findToDayWordList()).willReturn(this.list);

        // when
        final ResultActions action = mockMvc.perform(get("/word/test"))
                .andDo(print());

        //then
        action.andExpect(status().isOk())
                .andExpect(model().attribute("wordList", wordList))
                .andExpect(model().attribute("isExist", true))
                .andExpect(view().name("thymeleaf/word/viewWordTestForm"));
    }

    @Test
    void testGetRandomByUserWordList() throws Exception {
        //gvien
        given(this.service.getRandomWordList()).willReturn(this.list);
        String wordList = getWordList();

        //when
        final ResultActions action = mockMvc.perform(get("/word/test/random"))
                .andDo(print());

        //then
        action.andExpect(status().isOk())
                .andExpect(model().attribute("wordList", wordList))
                .andExpect(model().attribute("isExist", true))
                .andExpect(view().name("thymeleaf/word/viewWordTestForm"));

    }

    @Test
    void testGetRandomByAllWordList() throws Exception {
        //gvien
        given(this.service.getRandomByAllWordList()).willReturn(this.list);
        String wordList = getWordList();

        //when
        final ResultActions action = mockMvc.perform(get("/word/test/random")
                .param("all", "aa"))
                .andDo(print());

        //then
        action.andExpect(status().isOk())
                .andExpect(model().attribute("wordList", wordList))
                .andExpect(model().attribute("isExist", true))
                .andExpect(view().name("thymeleaf/word/viewWordTestForm"));
    }

    private String getWordList() {
        return "[{\"id\":0,\"word\":\"test\",\"meaning\":\"테스트\",\"nextDate\":\"2020-06-05\",\"level\":3,\"user\":null},{\"id\":0,\"word\":\"love\",\"meaning\":\"사랑\",\"nextDate\":\"2020-06-05\",\"level\":1,\"user\":null}]";
    }

    @Test
    void testCreateWordFail() throws Exception {
        // gvien
        BindingResult bindingResult = new MapBindingResult(new HashMap(), "");
        bindingResult.rejectValue("test", "test", "haha");

        given(this.service.getCreateWordBindingResult(any(WordDTO.AddWord.class), any(BindingResult.class))).willReturn(bindingResult);

        String addWordToJson = getAddWordToJson();

        //when
        ResultActions action = mockMvc.perform(post("/word/add")
                .param("save", "")
                .content(addWordToJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andDo(print());

        //then
        action.andExpect(status().isOk())
                .andExpect(view().name("thymeleaf/word/createWordForm"));
    }

    @Test
    void testCreateWordGood() throws Exception {
        // gvien
        BindingResult bindingResult = new MapBindingResult(new HashMap(), "");

        given(this.service.getCreateWordBindingResult(any(WordDTO.AddWord.class), any(BindingResult.class))).willReturn(bindingResult);

        String addWordToJson = getAddWordToJson();

        //when
        ResultActions action = mockMvc.perform(post("/word/add")
                .param("save", "")
                .content(addWordToJson)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andDo(print());

        //then
        action.andExpect(status().isOk())
                .andExpect(view().name("thymeleaf/index"));
    }

    private String getAddWordToJson() throws JsonProcessingException {
        WordDTO.AddWord addWord = new WordDTO.AddWord();
        addWord.setWord("haha");
        addWord.setMeaning("하하");

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(addWord);
    }

    @Test
    void updateNextDateAndInsert() throws Exception {
        // gvien
        String[] pass = new String[] {"1","2","3","4"};
        String[] fail = new String[] {"100","101"};

        List<String> failIds = Arrays.asList("틀란단어", "틀렸");

        given(this.service.insertFailWord(any())).willReturn(failIds);

        //when
        ResultActions action = mockMvc.perform(post("/word/answers")
                .param("pass", pass)
                .param("fail", fail)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .with(csrf()))
                .andDo(print());

        //then
        MvcResult result = action.andExpect(status().isOk())
        .andReturn();

        assertEquals("[\"틀란단어\",\"틀렸\"]", (result.getResponse().getContentAsString()));
    }
}