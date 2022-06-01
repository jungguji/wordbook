package com.jgji.spring.domain.user.service;

import com.jgji.spring.domain.user.domain.AccountAdapter;
import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.user.domain.UserDTO.UserProfile;
import com.jgji.spring.domain.user.repository.UserRepository;
import com.jgji.spring.global.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;
    private final UserSaveService userSaveService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new IllegalArgumentException());
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        AccountAdapter adapter = AccountAdapter.from(user);
        return adapter;
    }

    public boolean isExistName(String userName) {
        return !ObjectUtils.isEmpty(userRepository.findByUsername(userName));
    }
    
    public String changePassword(User user, UserProfile changePassword) {
        String validationmsg = getMessageChangePasswordValidation(user.getPassword(), changePassword);
        
        if (!StringUtils.isEmpty(validationmsg)) {
            return validationmsg;
        }

        user.changePassword(changePassword.getNewPassword());
        
        this.userSaveService.save(user);
        
        return "성공";
    }
    
    private String getMessageChangePasswordValidation(String currentPassword, UserProfile changePassword) {
        String oldPassword = changePassword.getOldPassword();
        
        if (isNullPassword(changePassword)) {
            return "비밀번호를 입력 해주십시오.";
        }
        
        if (isFailOldPasswordCompare(currentPassword, oldPassword)) {
            return "입력한 비밀번호와 현재 비밀번호가 일치하지 않습니다.";
        }
        
        String newPassword = changePassword.getNewPassword();
        
        if (isFailPasswordSize(newPassword)) {
            return "비밀번호는 6 자 이상이어야 합니다.";
        }
        
        if (isFailNewPasswordCompare(changePassword)) {
            return "변경 할 비밀번호가 일치하지 않습니다.";
        }
        
        return "";
    }
    
    private boolean isNullPassword(UserProfile changePassword) {
        return !(StringUtils.isEmpty(changePassword.getOldPassword()) || StringUtils.isEmpty(changePassword.getNewPassword())
                || StringUtils.isEmpty(changePassword.getNewPasswordConfrim()));
    }
    
    private boolean isFailOldPasswordCompare(String currentPassword, String oldPassword) {
        boolean result = false;
        
        if (!bcryptPasswordEncoder.matches(oldPassword, currentPassword)) {
            result = true;
        }
        
        return result;
    }
    
    private boolean isFailPasswordSize(String newPassword) {
        boolean result = false;
        if (newPassword.trim().length() < 6) {
            result = true;
        }
        
        return result;
    }
    
    private boolean isFailNewPasswordCompare(UserProfile changePassword) {
        boolean result = true;
        
        if (changePassword.getNewPassword().equals(changePassword.getNewPasswordConfrim())) {
            result = false;
        }
        
        return result;
    }
    
    public List<Map<String, Object>> getMostWrongWord(int userId) {
        List<Object[]> findList = userRepository.findMostWrongWord(userId);
        
        List<String> columns = new ArrayList<>();
        columns.add("mostWrongWord");
        columns.add("mostWrongCount");
        
        return Utils.convertListMap(findList, columns);
    }
}
