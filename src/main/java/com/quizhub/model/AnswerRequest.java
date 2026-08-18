package com.quizhub.model;

import java.util.Map;

public class AnswerRequest {
    // Map of questionId -> selectedOptionIndex
    private Map<Integer, Integer> answers;

    public AnswerRequest() {
    }

    public AnswerRequest(Map<Integer, Integer> answers) {
        this.answers = answers;
    }

    public Map<Integer, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Integer> answers) {
        this.answers = answers;
    }
}
