package com.example.my_first_telegram_bot.util;

import com.example.my_first_telegram_bot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionLoader {
    private final QuestionService questionService;

    @Scheduled(fixedDelay = 60000)
    public void reloadIfEmpty(){
        if(questionService.countQuestions() == 0){
            questionService.saveAll(QuestionData.getQuestions());
        }
    }
}
