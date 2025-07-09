package com.example.my_first_telegram_bot.handler;

import com.example.my_first_telegram_bot.bot.Button;
import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.example.my_first_telegram_bot.bot.Button.*;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class RegistrationHandler implements Handler {
    private final UserService userService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.equalsIgnoreCase(NAME_ACCEPT.getText()) || message.equalsIgnoreCase(NAME_CHANGE_CANCEL.getText())) {
            return accept(user);
        } else if (message.equalsIgnoreCase(NAME_CHANGE.getText())) {
            return changeName(user);
        }
        return checkName(user, message);
    }

    private List<PartialBotApiMethod<? extends Serializable>> accept(User user) {

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        final List<InlineKeyboardButton> inlineKeyboardButtonsRowOne =
                List.of(createInlineKeyboardButton("Start quiz", START_QUIZ));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        final SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText(String.format("Your name is saved as: %s", user.getName()));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        user.setBotState(State.PLAYING_QUIZ);
        userService.createOrUpdateUser(user);
        return List.of(sendMessage);
    }

    private List<PartialBotApiMethod<? extends Serializable>> changeName(User user) {
        user.setBotState(State.ENTER_NAME);
        userService.createOrUpdateUser(user);

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<InlineKeyboardButton> inlineKeyboardButtonRowOne =
                List.of(createInlineKeyboardButton("Cancel", NAME_CHANGE_CANCEL));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonRowOne));

        final SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText(String.format(
                "Your current name is: %s%nEnter new name or press the button to continue", user.getName()));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return List.of(sendMessage);
    }

    private List<PartialBotApiMethod<? extends Serializable>> checkName(User user, String message) {
        user.setName(message);
        userService.createOrUpdateUser(user);

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<InlineKeyboardButton> inlineKeyboardButtonRowOne =
                List.of(createInlineKeyboardButton("Accept", NAME_ACCEPT));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonRowOne));

        final SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText(String.format("You have entered: %s%nIf this is correct - press the button", user.getName()));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return List.of(sendMessage);
    }

    @Override
    public State operatedBotState() {
        return State.ENTER_NAME;
    }

    @Override
    public List<Button> operatedCallBackQuery() {
        return List.of(NAME_ACCEPT, NAME_CHANGE, NAME_CHANGE_CANCEL,START_QUIZ);
    }
}
