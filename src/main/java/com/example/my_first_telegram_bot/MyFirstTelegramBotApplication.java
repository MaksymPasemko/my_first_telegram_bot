package com.example.my_first_telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyFirstTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFirstTelegramBotApplication.class, args);
    }

}
