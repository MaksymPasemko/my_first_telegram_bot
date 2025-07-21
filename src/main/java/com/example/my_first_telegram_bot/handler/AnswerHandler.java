package com.example.my_first_telegram_bot.handler;

import com.example.my_first_telegram_bot.bot.Button;
import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.Question;
import com.example.my_first_telegram_bot.model.User;
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
import static com.example.my_first_telegram_bot.bot.State.PLAYING_QUIZ;
import static com.example.my_first_telegram_bot.handler.QuizHandler.*;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;


@Component
@RequiredArgsConstructor
public class AnswerHandler implements Handler {
    private final QuestionFolder questionFolder;
    private final UserService userService;


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        final Question checkQuestion = questionFolder.getLastQuestion(user.getChatId());
        questionFolder.clear(user.getChatId());

        final boolean isCorrect = isAnswerCorrect(message,checkQuestion);
        final SendMessage answerMessage = setAnswerMessage(user, isCorrect);

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        final InlineKeyboardButton stopButton = createInlineKeyboardButton("Stop quiz",STOP_QUIZ);
        final InlineKeyboardButton nextQuestionButton = createInlineKeyboardButton("Next question",NEXT_QUESTION);
        final List<InlineKeyboardButton> inlineKeyboardButtons = List.of(stopButton,nextQuestionButton);

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));
        answerMessage.setReplyMarkup(inlineKeyboardMarkup);

        user.setBotState(PLAYING_QUIZ);
        userService.createOrUpdateUser(user);
        return List.of(answerMessage);
    }

    private boolean isAnswerCorrect(String message,Question question){
        final String correctAnswer = question.getCorrectAnswer();
        if (message.toLowerCase().equals(OPTION_ONE.getText())) {
            return correctAnswer.equalsIgnoreCase(question.getOptionOne());
        } else if (message.toLowerCase().equals(OPTION_TWO.getText())) {
            return correctAnswer.equalsIgnoreCase(question.getOptionTwo());
        } else if (message.toLowerCase().equals(OPTION_THREE.getText())) {
            return correctAnswer.equalsIgnoreCase(question.getOptionThree());
        }
        return false;
    }

    private SendMessage setAnswerMessage(User user, boolean isCorrect) {
        final SendMessage answerMessage = createMessageTemplate(user);
        if(isCorrect){
            user.setScore(user.getScore() + 1);
            if(user.getHighScore() < user.getScore()){
                user.setHighScore(user.getScore());
            }
            answerMessage.setText(String.format("You got it right!%nYour score: " + user.getScore()
                    + "%nYour high score is " + user.getHighScore()));
        }else{
            answerMessage.setText(String.format("You got it wrong!%nYour score: " + user.getScore()
                    + "%nYour high score is " + user.getHighScore()));
        }
        return answerMessage;
    }
    @Override
    public State operatedBotState() {
        return State.ANSWER;
    }


}
