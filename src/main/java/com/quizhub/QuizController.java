package com.quizhub;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin
public class QuizController {

    // List to hold all quiz questions
    private List<Map<String, Object>> questions = new ArrayList<>();

    public QuizController() {
        // Initialize 5 simple Java questions
        questions.add(createQuestion(
                1,
                "Which keyword is used to create a class in Java?",
                "function",
                "class",
                "create",
                "object",
                "B"
        ));

        questions.add(createQuestion(
                2,
                "Which method is the starting point of a Java program?",
                "start()",
                "run()",
                "main()",
                "begin()",
                "C"
        ));

        questions.add(createQuestion(
                3,
                "Which data type is used to store whole numbers in Java?",
                "String",
                "int",
                "boolean",
                "double",
                "B"
        ));

        questions.add(createQuestion(
                4,
                "Which keyword is used to create an object in Java?",
                "new",
                "object",
                "create",
                "class",
                "A"
        ));

        questions.add(createQuestion(
                5,
                "Which symbol is used to end a statement in Java?",
                ".",
                ":",
                ";",
                ",",
                "C"
        ));
    }

    // Helper method to create a question map
    private Map<String, Object> createQuestion(int id, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        Map<String, Object> q = new HashMap<>();
        q.put("id", id);
        q.put("question", question);
        q.put("optionA", optionA);
        q.put("optionB", optionB);
        q.put("optionC", optionC);
        q.put("optionD", optionD);
        q.put("correctAnswer", correctAnswer);
        return q;
    }

    // Endpoint to get all quiz questions (without exposing correctAnswer)
    @GetMapping("/questions")
    public List<Map<String, Object>> getQuestions() {
        List<Map<String, Object>> questionList = new ArrayList<>();

        for (Map<String, Object> q : questions) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", q.get("id"));
            item.put("question", q.get("question"));
            item.put("optionA", q.get("optionA"));
            item.put("optionB", q.get("optionB"));
            item.put("optionC", q.get("optionC"));
            item.put("optionD", q.get("optionD"));
            questionList.add(item);
        }

        return questionList;
    }

    // Endpoint to submit user answers and calculate score
    @PostMapping("/submit")
    public Map<String, Object> submitQuiz(@RequestBody List<Map<String, Object>> answers) {
        int correctAnswers = 0;

        for (Map<String, Object> answer : answers) {
            int questionId = (Integer) answer.get("questionId");
            String selectedAnswer = (String) answer.get("selectedAnswer");

            for (Map<String, Object> q : questions) {
                int id = (Integer) q.get("id");
                if (id == questionId) {
                    String correctAnswer = (String) q.get("correctAnswer");
                    if (correctAnswer != null && correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                        correctAnswers++;
                    }
                    break;
                }
            }
        }

        int totalQuestions = questions.size();
        int score = (correctAnswers * 100) / totalQuestions;

        Map<String, Object> result = new HashMap<>();
        result.put("totalQuestions", totalQuestions);
        result.put("correctAnswers", correctAnswers);
        result.put("score", score);

        return result;
    }
}
