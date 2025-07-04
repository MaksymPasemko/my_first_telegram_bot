package com.example.my_first_telegram_bot.handler;

import ch.qos.logback.core.util.StringUtil;
import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.Question;
import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.service.QuestionService;
import com.example.my_first_telegram_bot.service.UserService;
import com.example.my_first_telegram_bot.util.QuestionFolder;
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
    private final QuestionService questionService;
    private final UserService userService;
    private final QuestionFolder questionFolder;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        final SendMessage questionMessage = createMessageTemplate(user);

        if(questionService.countQuestions() == 0){
            questionMessage.setText("Game over.Your score is " + user.getScore() +
                    "Your best score is " + user.getHighScore());
        }
        final Question randomQuestion = questionService.findRandomQuestion();
        questionService.delete(randomQuestion);

        questionMessage.setText(StringUtil.capitalizeFirstLetter(randomQuestion.getQuestion()));
        questionFolder.setLastQuestion(user.getChatId(),randomQuestion);

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

        user.setBotState(State.ANSWER);
        userService.createOrUpdateUser(user);
        return List.of(questionMessage);
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
