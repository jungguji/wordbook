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
        user.changePassword(bcryptPasswordEncoder.encode(user.getPassword()));

        return this.userRepository.save(user);
    }

    public String changeRandomPassword(User user) {
        User updateUser = this.userRepository.findByUsername(user.getUsername());

        return updateUser.changeRandomPassword();
    }

    public void changePassword(User user, UserDTO.UserProfile changePassword) {
        user.changePassword(changePassword.getNewPassword());
    }

}
