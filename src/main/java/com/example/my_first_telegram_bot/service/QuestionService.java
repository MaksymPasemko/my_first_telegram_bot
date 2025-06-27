package com.example.my_first_telegram_bot.service;

import com.example.my_first_telegram_bot.model.Question;
import com.example.my_first_telegram_bot.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> findQuestions() {
        return questionRepository.findAll();
    }

    public Question findRandomQuestion() {
        return questionRepository.findRandomQuestion();
    }

    public void saveAll(List<Question> questions) {
        questionRepository.saveAll(questions);
    }
}
