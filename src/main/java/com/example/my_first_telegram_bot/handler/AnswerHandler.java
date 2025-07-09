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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.example.my_first_telegram_bot.handler.QuizHandler.*;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.my_first_telegram_bot.util.TelegramUtil.createMessageTemplate;


@Component
@RequiredArgsConstructor
public class AnswerHandler implements Handler {
    private final QuestionFolder questionFolder;
    private final UserService userService;
    public static final String STOP_QUIZ = "/stop_quiz";
    public static final String NEXT_QUESTION = "/next_question";

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        final Question checkQuestion = questionFolder.getLastQuestion(user.getChatId());
        questionFolder.clear(user.getChatId());

        final boolean isCorrect = isAnswerCorrect(message,checkQuestion);
        final SendMessage answerMessage = setAnswerMessage(user, isCorrect);

        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        final InlineKeyboardButton stopButton = createInlineKeyboardButton("Stop",STOP_QUIZ);
        final InlineKeyboardButton nextQuestionButton = createInlineKeyboardButton("Next question",NEXT_QUESTION);
        final List<InlineKeyboardButton> inlineKeyboardButtons = List.of(stopButton,nextQuestionButton);

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtons));
        answerMessage.setReplyMarkup(inlineKeyboardMarkup);

        user.setBotState(State.PLAYING_QUIZ);
        userService.createOrUpdateUser(user);
        return List.of(answerMessage);
    }

    private boolean isAnswerCorrect(String message,Question question){
        final String correctAnswer = question.getCorrectAnswer();
        return switch (message.toLowerCase()){
            case OPTION_ONE -> correctAnswer.equalsIgnoreCase(question.getOptionOne());
            case OPTION_TWO -> correctAnswer.equalsIgnoreCase(question.getOptionTwo());
            case OPTION_THREE -> correctAnswer.equalsIgnoreCase(question.getOptionThree());
            default -> false;
        };
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

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(OPTION_ONE,OPTION_TWO,OPTION_THREE,STOP_QUIZ,NEXT_QUESTION);
    }
}
