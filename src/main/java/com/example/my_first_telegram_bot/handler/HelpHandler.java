package com.example.my_first_telegram_bot.handler;

import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.example.my_first_telegram_bot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;

@Component
public class HelpHandler implements Handler {
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        final List<InlineKeyboardButton> inlineKeyboardButtonRowOne =
                List.of(createInlineKeyboardButton("Change name", RegistrationHandler.NAME_CHANGE));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonRowOne));

        final SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText(String.format("You've asked for help %s?Here it comes!", user.getName()));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return List.of(sendMessage);
    }

    @Override
    public State operatedBotState() {
        return State.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
