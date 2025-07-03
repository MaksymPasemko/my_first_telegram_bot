package com.example.my_first_telegram_bot.handler;

import com.example.my_first_telegram_bot.bot.State;
import com.example.my_first_telegram_bot.model.Question;
import com.example.my_first_telegram_bot.model.User;
import com.example.my_first_telegram_bot.service.UserService;
import com.example.my_first_telegram_bot.util.QuestionFolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

import static com.example.my_first_telegram_bot.handler.QuizHandler.*;
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

        final String correctAnswer = checkQuestion.getCorrectAnswer();
        final boolean isCorrect = switch (message.toLowerCase()){
            case OPTION_ONE -> correctAnswer.equalsIgnoreCase(checkQuestion.getOptionOne());
            case OPTION_TWO -> correctAnswer.equalsIgnoreCase(checkQuestion.getOptionTwo());
            case OPTION_THREE -> correctAnswer.equalsIgnoreCase(checkQuestion.getOptionThree());
            default -> false;
        };

        final SendMessage answerMessage = createMessageTemplate(user);
        if(isCorrect){
            answerMessage.setText("You got it right!");
            user.setScore(user.getScore() + 1);
            if(user.getHighScore() < user.getScore()){
                user.setHighScore(user.getScore());
            }
        }else{
            answerMessage.setText("Answer is wrong!");
        }
        user.setBotState(State.PLAYING_QUIZ);
        userService.createOrUpdateUser(user);
        return List.of(answerMessage);
    }

    @Override
    public State operatedBotState() {
        return State.ANSWER;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of();
    }
}
