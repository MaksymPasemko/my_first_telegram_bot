package com.example.my_first_telegram_bot.util;

public class StringUtil {
    public String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase()
                + input.substring(1);
    }
}
