package com.example.my_first_telegram_bot.bot;

import com.example.my_first_telegram_bot.handler.Handler;
import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateReceiver {
    private final List<Handler> handlers;
    private final UserRepository userRepository;

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        final Long chatId;
        final String message;
        final User user;
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                message = update.getMessage().getText();
                chatId = update.getMessage().getChatId();
            } else if (update.hasCallbackQuery()) {
                message = update.getCallbackQuery().getData();
                chatId = update.getCallbackQuery().getMessage().getChatId();
            } else {
                throw new UnsupportedOperationException();
            }
            user = userRepository.getByChatId(chatId)
                    .orElseGet(() -> userRepository.save(new User(chatId)));

            return getHandlerByState(user.getBotState()).handle(user, message);
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(handler -> handler.operatedBotState() != null)
                .filter(handler -> handler.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

}
