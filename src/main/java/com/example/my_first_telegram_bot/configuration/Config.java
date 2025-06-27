package com.example.my_first_telegram_bot.configuration;

import com.example.my_first_telegram_bot.service.QuestionService;
import com.example.my_first_telegram_bot.util.QuestionData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public CommandLineRunner runner(QuestionService questionService) {
        return args -> {
            if (questionService.findQuestions().isEmpty()) {
                questionService.saveAll(QuestionData.getQuestions());
            }
        };
    }
}
