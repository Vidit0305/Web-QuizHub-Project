package com.quizhub.model;

public class Question {
    private int id;

    private String category;

    private String question;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer;

    public Question(){
    }

    public Question(
        int id,
        String category,
        String question,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        String correctAnswer){
        this.id = id;
        this.category = category;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    // getters
    public int getId() {
        return this.id;
    }

    public String getCategory() {
        return this.category;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getOptionA() {
        return this.optionA;
    }

    public String getOptionB() {
        return this.optionB;
    }

    public String getOptionC() {
        return this.optionC;
    }

    public String getOptionD() {
        return this.optionD;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    
}