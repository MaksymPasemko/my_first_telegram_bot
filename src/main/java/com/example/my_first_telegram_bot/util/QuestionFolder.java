package com.example.my_first_telegram_bot.util;

import com.example.my_first_telegram_bot.model.Question;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QuestionFolder {
    private final Map<Long, Question> questionMap = new HashMap<>();

    public void setLastQuestion(Long chatId,Question question){
        questionMap.put(chatId,question);
    }

    public Question getLastQuestion(Long chatId){
        return questionMap.get(chatId);
    }

    public void clear(Long chatId){
        questionMap.remove(chatId);
    }
}
