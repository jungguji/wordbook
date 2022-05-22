package com.jgji.spring.domain.user.dto;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.word.domain.Word;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Profile {

        private DefaultUserInfo user;
        private List<MyWord> word = new ArrayList<>();
        private List<Graph> graph = new ArrayList<>();

        @Builder
        public Profile(DefaultUserInfo user, List<MyWord> word, List<Graph> graph) {
            this.user = user;
            this.word = word;
            this.graph = graph;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DefaultUserInfo {
        private int id;
        private String username;

        @Builder
        public DefaultUserInfo(int id, String username) {
            this.id = id;
            this.username = username;
        }

        public static DefaultUserInfo of(User user) {
            return DefaultUserInfo.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyWord {
        private int id;
        private String word;
        private String meaning;
        private LocalDate nextDate;
        private int level;

        @Builder
        public MyWord(int id, String word, String meaning, LocalDate nextDate, int level) {
            this.id = id;
            this.word = word;
            this.meaning = meaning;
            this.nextDate = nextDate;
            this.level = level;
        }

        public static MyWord of(Word word) {
            return MyWord.builder()
                    .id(word.getId())
                    .word(word.getWord())
                    .meaning(word.getMeaning())
                    .nextDate(word.getNextDate())
                    .level(word.getLevel())
                    .build();
        }

        public static List<MyWord> ofList(List<Word> words) {
            List<MyWord> list = new ArrayList<>();

            for (Word word : words) {
                list.add(MyWord.of(word));
            }

            return list;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Graph {
        private String word;
        private BigInteger count;

        @Builder
        public Graph(String word, BigInteger count) {
            this.word = word;
            this.count = count;
        }

        public static Graph of(Map<String, Object> map) {
            return Graph.builder()
                    .word((String) map.get("word"))
                    .count((BigInteger) map.get("qty"))
                    .build();
        }

        public static List<Graph> ofList(List<Map<String, Object>> maps) {
            List<Graph> list = new ArrayList<>();

            for (Map<String, Object> map : maps) {
                list.add(Graph.of(map));
            }

            return list;
        }
    }
}
