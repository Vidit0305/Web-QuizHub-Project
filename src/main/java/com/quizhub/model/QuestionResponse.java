package com.quizhub.model;
public class QuestionResponse{
    private int id;
    private String category;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    public QuestionResponse(
        int id,
        String category,
        String question,
        String optionA,
        String optionB,
        String optionC,
        String optionD){

        this.id = id;
        this.category = category;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    //getters

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
}