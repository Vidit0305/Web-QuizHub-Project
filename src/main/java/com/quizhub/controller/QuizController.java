package com.quizhub.controller;

import com.quizhub.model.AnswerRequest;
import com.quizhub.model.QuestionResponse;
import com.quizhub.model.QuizResult;
import com.quizhub.service.QuizService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin
public class QuizController {

    private final QuizService quizService;


    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @GetMapping("/questions")
    public List<QuestionResponse> getQuestions() {
        return quizService.getAllQuestions();
    }


    @PostMapping("/submit")
    public QuizResult submitQuiz(
            @RequestBody List<AnswerRequest> answers) {

        return quizService.checkAnswers(answers);
    }
}