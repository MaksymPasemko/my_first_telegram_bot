package com.example.my_first_telegram_bot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "java_quiz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Question extends AbstractBaseEntity {
    @Column(name = "question", nullable = false)
    @NotBlank
    private String question;

    @Column(name = "correct_answer", nullable = false)
    @NotBlank
    private String correctAnswer;

    @Column(name = "option1", nullable = false)
    @NotBlank
    private String optionOne;

    @Column(name = "option2", nullable = false)
    @NotBlank
    private String optionTwo;

    @Column(name = "option3", nullable = false)
    @NotBlank
    private String optionThree;
}
