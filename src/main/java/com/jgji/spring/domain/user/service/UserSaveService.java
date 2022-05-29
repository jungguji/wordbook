package com.jgji.spring.domain.user.service;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.user.domain.UserDTO;
import com.jgji.spring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSaveService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    public User save(User user) {
        user.update(bcryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public String setTempPassWord(User user) {
        User updateUser = this.userRepository.findByUsername(user.getUsername());
        String tempPassword = getTempPassword();

        updateUser.update(tempPassword);

        return tempPassword;
    }

    private String getTempPassword() {
        char passwordArray[] = new char[] {
                '0','1','2','3','4','5','6','7','8','9',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '!','@','#','$','%','^','&','*','(',')'};

        StringBuilder ranPw = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int selectRandomPw = (int)(Math.random()*(passwordArray.length));
            ranPw.append(passwordArray[selectRandomPw]);
        }

        return ranPw.toString();
    }

    public void changePassword(User user, UserDTO.UserProfile changePassword) {
        user.update(changePassword.getNewPassword());

        return "성공";
    }

}
