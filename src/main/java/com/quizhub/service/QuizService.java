package com.quizhub.service;

import com.quizhub.model.Question;
import com.quizhub.model.QuestionResponse;
import com.quizhub.model.AnswerRequest;
import com.quizhub.model.QuizResult;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private List<Question> questions = new ArrayList<>();


    public QuizService() {

        questions.add(new Question(
                1,
                "Java",
                "Which keyword is used to create a class in Java?",
                "function",
                "class",
                "create",
                "object",
                "B"
        ));

        questions.add(new Question(
                2,
                "Java",
                "Which method is the starting point of a Java program?",
                "start()",
                "run()",
                "main()",
                "begin()",
                "C"
        ));

        questions.add(new Question(
                3,
                "Java",
                "Which data type is used to store whole numbers?",
                "String",
                "int",
                "boolean",
                "double",
                "B"
        ));

        questions.add(new Question(
                4,
                "Java",
                "Which keyword is used to create an object?",
                "new",
                "object",
                "create",
                "class",
                "A"
        ));

        questions.add(new Question(
                5,
                "Java",
                "Which symbol is used to end a statement in Java?",
                ".",
                ":",
                ";",
                ",",
                "C"
        ));
    }


    public List<QuestionResponse> getAllQuestions() {

        List<QuestionResponse> responses = new ArrayList<>();

        for (Question question : questions) {

            QuestionResponse response = new QuestionResponse(
                    question.getId(),
                    question.getCategory(),
                    question.getQuestion(),
                    question.getOptionA(),
                    question.getOptionB(),
                    question.getOptionC(),
                    question.getOptionD()
            );

            responses.add(response);
        }

        return responses;
    }


    public QuizResult checkAnswers(List<AnswerRequest> answers) {

        int correctAnswers = 0;

        for (AnswerRequest answer : answers) {

            for (Question question : questions) {

                if (question.getId() == answer.getQuestionId()) {

                    if (question.getCorrectAnswer()
                            .equals(answer.getSelectedAnswer())) {

                        correctAnswers++;
                    }

                    break;
                }
            }
        }

        int totalQuestions = questions.size();

        int score = (correctAnswers * 100) / totalQuestions;

        return new QuizResult(
                totalQuestions,
                correctAnswers,
                score
        );
    }
}