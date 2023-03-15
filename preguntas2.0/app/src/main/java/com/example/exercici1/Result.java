package com.example.exercici1;

import java.io.Serializable;

public class Result implements Serializable {
    private String messageCorrect;
    private String messageIncorrect;
    private int resultCorrect;
    private int resultIncorrect;

    public Result(String messageCorrect, String messageIncorrect, int resultCorrect, int resultIncorrect) {
        this.messageCorrect = messageCorrect;
        this.messageIncorrect = messageIncorrect;
        this.resultCorrect = resultCorrect;
        this.resultIncorrect = resultIncorrect;
    }

    public String messageResultCorrect(){
        return messageCorrect +" "+ resultCorrect;
    }

    public String messageResultIncorrect(){
        return messageIncorrect + " " + resultIncorrect;
    }

    public int getResultCorrect() {
        return resultCorrect;
    }

    public int getResultIncorrect() {
        return resultIncorrect;
    }
}
