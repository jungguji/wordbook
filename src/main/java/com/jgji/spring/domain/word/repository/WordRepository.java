package com.jgji.spring.domain.word.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jgji.spring.domain.word.model.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer>, WordRepositoryCustom {

    List<Word> findByUserId(String userId);
    
    List<Word> findByUserIdAndNextDateLessThanEqual(String userId, LocalDate nextDate);
    
    @Query("SELECT w FROM Word w ORDER BY RAND()")
    List<Word> findOrderByRandom();
    
    @Query("SELECT w FROM Word w JOIN w.user u WHERE u.id = :userId ORDER BY RAND()")
    List<Word> findByUserIdOrderByRandom(@Param("userId") String userId);
    
    List<Word> findByIdIn(List<Integer> wordIdList);
    
    void deleteByIdIn(List<Integer> id);
}
