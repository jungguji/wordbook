package com.jgji.spring.domain.word.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class WordRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TestWord {

        private List<Integer> pass;
        private List<Integer> fail;
    }
}
