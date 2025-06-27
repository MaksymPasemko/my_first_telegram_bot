package com.example.my_first_telegram_bot.dto.dtorequest;

import com.example.my_first_telegram_bot.bot.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDtoRequest {
    private Long chatId;
    private Integer score;
    private Integer highScore;
    private String name;
    private State botState;
}
