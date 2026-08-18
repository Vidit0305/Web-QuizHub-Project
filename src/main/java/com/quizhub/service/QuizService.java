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

    private final List<Question> questions = new ArrayList<>();

    public QuizService() {

        // Java Questions

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
                "this",
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

        // Python Questions

        questions.add(new Question(
                6,
                "Python",
                "Which keyword is used to define a function in Python?",
                "function",
                "def",
                "fun",
                "define",
                "B"
        ));

        questions.add(new Question(
                7,
                "Python",
                "Which symbol is used for a comment in Python?",
                "//",
                "/*",
                "#",
                "<!--",
                "C"
        ));

        // General Knowledge

        questions.add(new Question(
                8,
                "General",
                "What is the capital of India?",
                "Mumbai",
                "New Delhi",
                "Kolkata",
                "Chennai",
                "B"
        ));

        questions.add(new Question(
                9,
                "General",
                "Which planet is known as the Red Planet?",
                "Earth",
                "Venus",
                "Mars",
                "Jupiter",
                "C"
        ));

        questions.add(new Question(
                10,
                "General",
                "How many continents are there on Earth?",
                "Five",
                "Six",
                "Seven",
                "Eight",
                "C"
        ));
    }

    public List<QuestionResponse> getQuestions(String category) {

        List<QuestionResponse> result = new ArrayList<>();

        for (Question question : questions) {

            if (category == null ||
                    question.getCategory().equalsIgnoreCase(category)) {

                result.add(new QuestionResponse(
                        question.getId(),
                        question.getCategory(),
                        question.getQuestion(),
                        question.getOptionA(),
                        question.getOptionB(),
                        question.getOptionC(),
                        question.getOptionD()
                ));
            }
        }

        return result;
    }

    public QuizResult calculateResult(List<AnswerRequest> answers) {

        int correct = 0;

        for (AnswerRequest answer : answers) {

            for (Question question : questions) {

                if (question.getId() == answer.getQuestionId()) {

                    if (question.getCorrectAnswer()
                            .equalsIgnoreCase(answer.getSelectedAnswer())) {

                        correct++;
                    }

                    break;
                }
            }
        }

        int total = answers.size();

        int wrong = total - correct;

        double percentage = 0;

        if (total > 0) {
            percentage = ((double) correct / total) * 100;
        }

        return new QuizResult(
                total,
                correct,
                wrong,
                percentage
        );
    }
}