package com.jgji.spring.domain.user.service;

import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.user.dto.UserRequest;
import com.jgji.spring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserSaveService {

    private final UserRepository userRepository;

    public User save(User user) {
        user.changePassword(user.getPassword());

        return this.userRepository.save(user);
    }

    public String changeRandomPassword(String username) {
        User user = this.userRepository.findByUsername(username);
        return user.changeRandomPassword();
    }

    public void changePassword(User user, UserRequest.ChangePassword changePassword) {
        user.changePassword(changePassword.getNewPassword());
        this.userRepository.save(user);
    }

}
