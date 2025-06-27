package com.example.my_first_telegram_bot.dto.dtoresponse;

import com.example.my_first_telegram_bot.bot.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDtoResponse {
    private Long id;
    private Long chatId;
    private Integer score;
    private Integer highScore;
    private String name;
    private State botState;
}
