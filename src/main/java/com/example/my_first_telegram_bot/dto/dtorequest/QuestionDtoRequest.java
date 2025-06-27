package com.example.my_first_telegram_bot.dto.dtorequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionDtoRequest {
    private String question;
    private String correctAnswer;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
}
