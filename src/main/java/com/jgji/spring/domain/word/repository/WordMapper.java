package com.jgji.spring.domain.word.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jgji.spring.domain.word.model.Word;

@Mapper
public interface WordMapper {
//    @Select("SELECT w.id , w.word , w.meaning , w.next_date FROM Word w ")
    List<Word> findAllByUserId(String userId);
}
