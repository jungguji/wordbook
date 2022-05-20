package com.jgji.spring.domain.user;

import com.jgji.spring.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponse {

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
}
