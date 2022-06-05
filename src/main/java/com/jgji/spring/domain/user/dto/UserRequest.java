package com.jgji.spring.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UserRequest {

    @Getter
    public static class CreateUser {
        private String id;

        @NotEmpty
        private String username;

        @Size(min=6, message="비밀번호는 6 자 이상이어야 합니다." )
        private String password;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserProfile {
        private String userName;
        private List<String> mostWrongWord = new ArrayList<>();
        private List<BigInteger> mostWrongCount = new ArrayList<>();
        private ChangePassword changePassword;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChangePassword {
        private String oldPassword;

        @Size(min=6, message="비밀번호는 6 자 이상이어야 합니다." )
        private String newPassword;

        @Size(min=6, message="비밀번호는 6 자 이상이어야 합니다." )
        private String newPasswordConfirm;

        @JsonCreator
        public ChangePassword(String oldPassword, String newPassword, String newPasswordConfirm) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            Assert.isTrue(newPassword.equals(newPasswordConfirm), "The password to be changed does not match.");
            Assert.isTrue(bCryptPasswordEncoder.matches(bCryptPasswordEncoder.encode(oldPassword), oldPassword), "Not the same as previous password.");

            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.newPasswordConfirm = newPasswordConfirm;
        }
    }
}
