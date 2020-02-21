package com.jgji.spring.domain.word.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.jgji.spring.domain.word.model.Word;

@Mapper
public interface WordMapper {
//    @Select("SELECT w.id , w.word , w.meaning , w.next_date FROM Word w ")
    List<Word> findAllByUserId(String userId);
    
    List<Word> findToDayWordListByUserId(@Param("id") String userId, @Param("date") LocalDate nextDate);
    
    @Select("SELECT * FROM Word w WHERE w.users_id = #{userId} ORDER BY RAND() limit 10")
    List<Word> findRandomWordListByUserId(@Param("userId") String userId);
    
    @Select("SELECT * FROM Word w ORDER BY RAND()")
    List<Word> getRandomByAllWordList();
    
    List<Word> findWordListByUserIdAndWordId(Map<String, Object> map);
    
    void updateSuccessWord(Word word);
    
    @Insert("INSERT INTO Word(word, meaning, next_date, users_id) VALUES(#{word.word}, #{word.meaning}, #{word.nextDate}, ${word.usersId})")
    void insertFailWord(@Param("word") Word word);
}
