package com.jgji.spring.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.global.error.exception.BusinessException;
import com.jgji.spring.global.error.exception.ErrorCode;
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

        @Size(min = 6, message = "비밀번호는 6 자 이상이어야 합니다.")
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
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String newPassword;
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String newPasswordConfirm;

        public void validation(User user) {
            if (!newPassword.equals(newPasswordConfirm)) {
                throw new BusinessException(ErrorCode.CHANGED_DOES_NOT_MATCH);
            }

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if (!bCryptPasswordEncoder.matches(this.oldPassword, user.getPassword())) {
                throw new BusinessException(ErrorCode.NOT_SAME_PREVIOUS_PASSWORD);
            }
        }
    }
}
