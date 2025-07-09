package com.example.my_first_telegram_bot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Button {
    STOP_QUIZ("/stop_quiz"),
    NEXT_QUESTION("/next_question"),
    HELP("/help"),
    OPTION_ONE("/option_one"),
    OPTION_TWO("/option_two"),
    OPTION_THREE("/option_three"),
    START_QUIZ("/start_quiz"),
    NAME_ACCEPT("/enter_name_accept"),
    NAME_CHANGE("/enter_name"),
    NAME_CHANGE_CANCEL("/enter_name_cancel");

    private final String text;

}
