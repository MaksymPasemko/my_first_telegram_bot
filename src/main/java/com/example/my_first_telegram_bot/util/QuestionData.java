package com.example.my_first_telegram_bot.util;

import com.example.my_first_telegram_bot.model.Question;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class QuestionData {
    public static List<Question> getQuestions() {
        return List.of(
                new Question(
                        "What is the size of int in Java?",
                        "4 bytes",
                        "2 bytes",
                        "4 bytes",
                        "8 bytes"
                ), new Question(
                        "Which keyword is used to inherit a class in Java?",
                        "extends",
                        "super",
                        "extends",
                        "implements"
                ), new Question(
                        "Which method is the entry point of a Java application?",
                        "main",
                        "start",
                        "main",
                        "run"
                ), new Question(
                        "What is JVM?",
                        "Java Virtual Machine",
                        "Java Visual Machine",
                        "Java Virtual Machine",
                        "Java Verified Method"
                ), new Question(
                        "Which operator is used for comparison?",
                        "==",
                        "=",
                        "==",
                        "!="
                ), new Question(
                        "What is the default value of a boolean variable?",
                        "false",
                        "true",
                        "false",
                        "null"
                ), new Question(
                        "What does 'static' keyword mean?",
                        "Belongs to the class",
                        "Belongs to the object",
                        "Belongs to the class",
                        "Is changeable"
                ), new Question(
                        "Which collection allows duplicate elements?",
                        "List",
                        "Set",
                        "List",
                        "Map"
                ), new Question(
                        "What does 'final' keyword mean in Java?",
                        "Cannot be changed",
                        "Can be overridden",
                        "Cannot be changed",
                        "Can be extended"
                ), new Question(
                        "What is the parent class of all Java classes?",
                        "Object",
                        "Class",
                        "Object",
                        "Main"
                )
        );
    }
}
