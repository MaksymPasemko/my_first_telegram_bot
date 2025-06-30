package com.example.my_first_telegram_bot.handler;

import ch.qos.logback.core.util.StringUtil;
import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.Question;
import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.example.my_first_telegram_bot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class QuizHandler implements Handler {
    public static final String OPTION_ONE = "/option_one";
    public static final String OPTION_TWO = "/option_two";
    public static final String OPTION_THREE = "/option_three";
    public static final String CORRECT_ANSWER = "/option_three";
    private final QuestionService questionService;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        final SendMessage questionMessage = createMessageTemplate(user);
        final Question randomQuestion = questionService.findRandomQuestion();
        questionMessage.setText(StringUtil.capitalizeFirstLetter(randomQuestion.getQuestion()));

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final InlineKeyboardButton optionOneButton =
                createInlineKeyboardButton(randomQuestion.getOptionOne(), OPTION_ONE);
        final InlineKeyboardButton optionTwoButton =
                createInlineKeyboardButton(randomQuestion.getOptionTwo(), OPTION_TWO);
        final InlineKeyboardButton optionThreeButton =
                createInlineKeyboardButton(randomQuestion.getOptionThree(), OPTION_THREE);

        final List<InlineKeyboardButton> inlineKeyboardButtons =
                List.of(optionOneButton, optionTwoButton, optionThreeButton);
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));
        questionMessage.setReplyMarkup(inlineKeyboardMarkup);

        final SendMessage answerMessage = createMessageTemplate(user);
        final String answer = handleAnswer(user, message);
        answerMessage.setText(answer);

        return List.of(questionMessage, answerMessage);
    }

    public String handleAnswer(User user, String message) {
        if (message.equalsIgnoreCase(CORRECT_ANSWER)) {
            user.setScore(user.getScore() + 1);
            return "Answer is correct";
        }
        return "Answer is wrong";
    }

    @Override
    public State operatedBotState() {
        return State.PLAYING_QUIZ;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(OPTION_ONE, OPTION_TWO, OPTION_THREE);
    }
}
