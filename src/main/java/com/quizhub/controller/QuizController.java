package com.quizhub.controller;

import com.quizhub.model.AnswerRequest;
import com.quizhub.model.QuestionResponse;
import com.quizhub.model.QuizResult;
import com.quizhub.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions() {
        return ResponseEntity.ok(quizService.getAllQuestions());
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizResult> submitQuiz(@RequestBody AnswerRequest answerRequest) {
        QuizResult result = quizService.evaluateQuiz(answerRequest);
        return ResponseEntity.ok(result);
    }
}
