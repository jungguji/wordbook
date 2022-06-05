package com.jgji.spring.domain.user.service;

import com.jgji.spring.domain.user.domain.AccountAdapter;
import com.jgji.spring.domain.user.domain.User;
import com.jgji.spring.domain.user.dto.UserRequest;
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
    
    public List<Map<String, Object>> getMostWrongWord(int userId) {
        List<Object[]> findList = userRepository.findMostWrongWord(userId);
        
        List<String> columns = new ArrayList<>();
        columns.add("mostWrongWord");
        columns.add("mostWrongCount");
        
        return Utils.convertListMap(findList, columns);
    }
}
