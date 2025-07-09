package com.example.my_first_telegram_bot.handler;

import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

import static com.example.my_first_telegram_bot.bot.State.*;
import static com.example.my_first_telegram_bot.handler.RegistrationHandler.START_QUIZ;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class IDLEHandler implements Handler{
    private final UserService userService;
    private final HelpHandler helpHandler;
    private final QuizHandler quizHandler;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if(message.equalsIgnoreCase(START_QUIZ)){
            user.setBotState(PLAYING_QUIZ);
            userService.createOrUpdateUser(user);
            return quizHandler.handle(user,message);
        } else if (message.equalsIgnoreCase(QuizHandler.HELP)) {
            user.setBotState(State.HELP);
            userService.createOrUpdateUser(user);
            return helpHandler.handle(user,message);
        }

        final SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText("Please enter help or start quiz!");
        user.setBotState(IDLE);
        userService.createOrUpdateUser(user);

        return List.of(sendMessage);
    }

    @Override
    public State operatedBotState() {
        return IDLE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(START_QUIZ,QuizHandler.HELP);
    }
}
