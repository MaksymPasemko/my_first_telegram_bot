package com.example.my_first_telegram_bot.handler;

import ch.qos.logback.core.util.StringUtil;
import com.example.my_first_telegram_bot.bot.Button;
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

import static com.example.my_first_telegram_bot.bot.Button.*;
import static com.example.my_first_telegram_bot.bot.State.IDLE;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;

@Component
@RequiredArgsConstructor
public class QuizHandler implements Handler {
    private final QuestionService questionService;
    private final UserService userService;
    private final QuestionFolder questionFolder;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        if (message.equalsIgnoreCase(STOP_QUIZ.getText())){
            return stopQuiz(user);
        }
        return newQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> stopQuiz(User user){
        final SendMessage resultsMessage = createMessageTemplate(user);
        resultsMessage.setText(String.format("Quiz stopped%nYou got score: " + user.getScore()
                + "%nYour high score is " + user.getHighScore()));

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        final InlineKeyboardButton startQuizButton = createInlineKeyboardButton("Start quiz",START_QUIZ);
        final InlineKeyboardButton helpButton = createInlineKeyboardButton("Help",HELP);
        final List<InlineKeyboardButton> inlineKeyboardButtons = List.of(startQuizButton,helpButton);
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));

        resultsMessage.setReplyMarkup(inlineKeyboardMarkup);

        user.setScore(0);
        user.setBotState(IDLE);
        userService.createOrUpdateUser(user);

        return List.of(resultsMessage);
    }

    private List<PartialBotApiMethod<? extends Serializable>> newQuestion(User user){
        final SendMessage questionMessage = createMessageTemplate(user);

        if(questionService.countQuestions() == 0){
            questionMessage.setText(String.format("Game over.%nYour score is " + user.getScore() +
                    "%nYour best score is " + user.getHighScore()));
            user.setScore(0);
            userService.createOrUpdateUser(user);
            return List.of(questionMessage);
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


}
