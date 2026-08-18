package com.quizhub.model;

import java.util.Map;

public class QuizResult {
    private int totalQuestions;
    private int correctAnswers;
    private int scorePercentage;
    private String feedback;
    private Map<Integer, Integer> correctOptions;

    public QuizResult() {
    }

    public QuizResult(int totalQuestions, int correctAnswers, int scorePercentage, String feedback, Map<Integer, Integer> correctOptions) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.scorePercentage = scorePercentage;
        this.feedback = feedback;
        this.correctOptions = correctOptions;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(int scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Map<Integer, Integer> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(Map<Integer, Integer> correctOptions) {
        this.correctOptions = correctOptions;
    }
}
