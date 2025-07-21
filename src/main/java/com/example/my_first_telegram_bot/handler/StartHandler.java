package com.example.my_first_telegram_bot.handler;

import com.example.my_first_telegram_bot.bot.Button;
import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {
    @Value("${bot.name}")
    private String botUsername;
    private final UserRepository userRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        final SendMessage welcomeMessage = createMessageTemplate(user);
        welcomeMessage.setText(String.format("Hola!I'm *%s*%nI am here to help you learn Java", botUsername));

        final SendMessage registrationMessage = createMessageTemplate(user);
        registrationMessage.setText("In order to start our journey tell me your name");

        user.setBotState(State.ENTER_NAME);
        userRepository.save(user);

        return List.of(welcomeMessage, registrationMessage);
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

}
