package com.example.my_first_telegram_bot.repository;

import com.example.my_first_telegram_bot.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM java_quiz ORDER BY random() LIMIT 1")
    Question findRandomQuestion();
}
