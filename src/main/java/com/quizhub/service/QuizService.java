package com.quizhub.service;

import com.quizhub.model.AnswerRequest;
import com.quizhub.model.Question;
import com.quizhub.model.QuestionResponse;
import com.quizhub.model.QuizResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final List<Question> questionBank = new ArrayList<>();

    public QuizService() {
        initSampleQuestions();
    }

    private void initSampleQuestions() {
        questionBank.add(new Question(
                1,
                "Which programming language is known as the backbone of Spring Boot?",
                List.of("Python", "Java", "C++", "JavaScript"),
                1
        ));
        questionBank.add(new Question(
                2,
                "What is the default port for an embedded Tomcat server in Spring Boot?",
                List.of("8080", "3000", "5000", "8000"),
                0
        ));
        questionBank.add(new Question(
                3,
                "Which annotation marks a class as a Spring Boot entry point?",
                List.of("@Controller", "@SpringBootApplication", "@Service", "@Component"),
                1
        ));
        questionBank.add(new Question(
                4,
                "Which HTTP method is commonly used to submit quiz answers?",
                List.of("GET", "DELETE", "POST", "HEAD"),
                2
        ));
        questionBank.add(new Question(
                5,
                "What format is most commonly used for data exchange in modern RESTful APIs?",
                List.of("XML", "JSON", "CSV", "YAML"),
                1
        ));
    }

    public List<QuestionResponse> getAllQuestions() {
        return questionBank.stream()
                .map(q -> new QuestionResponse(q.getId(), q.getQuestion(), q.getOptions()))
                .collect(Collectors.toList());
    }

    public QuizResult evaluateQuiz(AnswerRequest answerRequest) {
        int total = questionBank.size();
        int correctCount = 0;
        Map<Integer, Integer> correctOptionsMap = new HashMap<>();
        Map<Integer, Integer> userAnswers = answerRequest != null && answerRequest.getAnswers() != null
                ? answerRequest.getAnswers()
                : new HashMap<>();

        for (Question q : questionBank) {
            correctOptionsMap.put(q.getId(), q.getCorrectOptionIndex());
            Integer userSelection = userAnswers.get(q.getId());
            if (userSelection != null && userSelection == q.getCorrectOptionIndex()) {
                correctCount++;
            }
        }

        int percentage = total > 0 ? (int) Math.round(((double) correctCount / total) * 100) : 0;
        String feedback;
        if (percentage == 100) {
            feedback = "Outstanding! Perfect score! 🎉";
        } else if (percentage >= 80) {
            feedback = "Great job! You really know your stuff! 🚀";
        } else if (percentage >= 50) {
            feedback = "Good effort! Keep practicing to sharpen your skills. 👍";
        } else {
            feedback = "Keep learning and try again! You can do it! 💪";
        }

        return new QuizResult(total, correctCount, percentage, feedback, correctOptionsMap);
    }
}
