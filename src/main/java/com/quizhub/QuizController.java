package com.quizhub;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin
public class QuizController {

    private List<Map<String, Object>> questions = new ArrayList<>();

    public QuizController() {
        addQuestion(1, "Which keyword is used to create a class in Java?", "function", "class", "create", "object", "B");
        addQuestion(2, "Which method is the starting point of a Java program?", "start()", "run()", "main()", "begin()", "C");
        addQuestion(3, "Which data type is used to store whole numbers in Java?", "String", "int", "boolean", "double", "B");
        addQuestion(4, "Which keyword is used to create an object in Java?", "new", "object", "create", "class", "A");
        addQuestion(5, "Which symbol is used to end a statement in Java?", ".", ":", ";", ",", "C");
    }

    private void addQuestion(int id, String question, String a, String b, String c, String d, String answer) {
        Map<String, Object> q = new HashMap<>();
        q.put("id", id);
        q.put("question", question);
        q.put("optionA", a);
        q.put("optionB", b);
        q.put("optionC", c);
        q.put("optionD", d);
        q.put("correctAnswer", answer);
        questions.add(q);
    }

    @GetMapping("/questions")
    public List<Map<String, Object>> getQuestions() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> q : questions) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", q.get("id"));
            item.put("question", q.get("question"));
            item.put("optionA", q.get("optionA"));
            item.put("optionB", q.get("optionB"));
            item.put("optionC", q.get("optionC"));
            item.put("optionD", q.get("optionD"));
            list.add(item);
        }
        return list;
    }

    @PostMapping("/submit")
    public Map<String, Object> submitQuiz(@RequestBody List<Map<String, Object>> answers) {
        int correct = 0;

        for (Map<String, Object> answer : answers) {
            int questionId = (Integer) answer.get("questionId");
            String selected = (String) answer.get("selectedAnswer");

            for (Map<String, Object> q : questions) {
                if ((Integer) q.get("id") == questionId) {
                    if (q.get("correctAnswer").toString().equalsIgnoreCase(selected)) {
                        correct++;
                    }
                    break;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalQuestions", questions.size());
        result.put("correctAnswers", correct);
        result.put("score", (correct * 100) / questions.size());
        return result;
    }
}
