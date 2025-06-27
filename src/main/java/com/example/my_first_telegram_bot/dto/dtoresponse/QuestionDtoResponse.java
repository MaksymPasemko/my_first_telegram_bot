package com.example.my_first_telegram_bot.dto.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionDtoResponse {
    private Long id;
    private String question;
    private String correctAnswer;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
}
