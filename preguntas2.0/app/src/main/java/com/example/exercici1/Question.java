package com.example.exercici1;

public class Question {
    private String question;
    private boolean answer;
    private boolean status;
    public Question(String question, boolean answer) {
        this.question = question;
        this.answer = answer;
        this.status = false;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isAnswer() {
        return answer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
