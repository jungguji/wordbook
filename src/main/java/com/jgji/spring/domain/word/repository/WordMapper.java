package com.jgji.spring.domain.word.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jgji.spring.domain.word.model.Word;

@Mapper
public interface WordMapper {
//    @Select("SELECT w.id , w.word , w.meaning , w.next_date FROM Word w ")
    List<Word> findAllByUserId(String userId);
    
    List<Word> findToDayWordListByUserId(@Param("id") String userId, @Param("date") LocalDate nextDate);
    
    @Select("SELECT * FROM Word w WHERE w.users_id = #{userId} ORDER BY RAND() limit 10")
    List<Word> findRandomWordListByUserId(@Param("userId") String userId);
    
    @Select("SELECT * FROM Word w ORDER BY RAND() LIMIT 10")
    List<Word> getRandomByAllWordList();
    
    List<Word> findWordListByUserIdAndWordId(List<String> list);
    
    @Select(
      "    SELECT                      "
    + "        word AS word            "
    + "        , COUNT(1) AS qty       "
    + "    FROM                        "
    + "        word                    "
    + "    WHERE                       "
    + "        users_id = ${userId}    "
    + "    GROUP BY                    "
    + "        word                    "
    + "    ORDER BY qty DESC           "
    + "    LIMIT 10                    "
    )
    @Results(value = {
            @Result(property = "word", column = "word")
            , @Result(property = "qty", column = "qty")
    })
    List<Map<String, Object>> findFrequentFailWord(@Param("userId") String userId);
    
    @Select(" SELECT                                        " + 
            "     w.word as word                            " +
            "     , COUNT(1) as qty                         " + 
            " FROM                                          " + 
            "     Word w                                    " + 
            " WHERE                                         " +
            "    w.users_id = ${userId}                     " + 
            " GROUP BY                                      " + 
            "     w.word                                    " + 
            " HAVING                                        " + 
            "     COUNT(1) = (                              " + 
            "             SELECT                            " + 
            "                 MAX(cnt)                      " + 
            "             FROM (                            " + 
            "                     SELECT                    " + 
            "                         COUNT(1) AS cnt       " + 
            "                     FROM                      " + 
            "                         Word w                " + 
            "                     WHERE                     " + 
            "                         w.users_id = ${userId}" + 
            "                     GROUP BY                  " + 
            "                         w.word                " + 
            "                  ) AS maxfrom                 " + 
            "             )                                 ")
    @Results(value = {
            @Result(property = "word", column = "word")
            , @Result(property = "qty", column = "qty")
    })
     List<Map<String, Object>> findMostWrongWord(@Param("userId") String userId);
    
    void updateSuccessWord(Word word);
    
    @Update("UPDATE Word SET meaning = #{meaning} WHERE id = ${id}")
    void updateMeaning(@Param("meaning") String meaning, @Param("id") int id);
    
    @Insert("INSERT INTO Word(word, meaning, next_date, users_id) VALUES(#{word.word}, #{word.meaning}, #{word.nextDate}, ${word.usersId})")
    void insertWord(@Param("word") Word word);
    
    void insertBatchWord(List<Word> list);
    
    void delete(List<String> list);
    
    
}
