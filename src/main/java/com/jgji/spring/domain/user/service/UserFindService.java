package com.jgji.spring.domain.user.service;

import com.jgji.spring.domain.user.repository.UserRepository;
import com.jgji.spring.global.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserFindService {

    private final UserRepository userRepository;

    public List<Map<String, Object>> findMostWrongWord(int userId) {
        List<Object[]> findList = userRepository.findMostWrongWord(userId);

        List<String> columns = new ArrayList<>();
        columns.add("mostWrongWord");
        columns.add("mostWrongCount");

        return Utils.convertListMap(findList, columns);
    }
}
