package com.quizhub.model;

public class QuizResult {

    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private double percentage;

    public QuizResult(
            int totalQuestions,
            int correctAnswers,
            int wrongAnswers,
            double percentage) {

        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.percentage = percentage;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public double getPercentage() {
        return percentage;
    }
}