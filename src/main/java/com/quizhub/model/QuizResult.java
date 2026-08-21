package com.quizhub.model;

public class QuizResult {

    private int totalQuestions;

    private int correctAnswer;

    private int score;

    public QuizResult(int totalQuestions,

            int correctAnswers,

            int score) {

        this.totalQuestions = totalQuestions;

        this.correctAnswers = correctAnswers;

        this.score = score;
    }

    public int getTotalQuestions(){

        return totalQuestions;
    }

    public int getCorrectAnswers(){

        return correctAnswers;
    }

    public int getScore(){

        return score;
    }
}