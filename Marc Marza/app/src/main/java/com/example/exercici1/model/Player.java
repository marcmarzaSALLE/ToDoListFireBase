package com.example.exercici1.model;

import java.io.Serializable;

public class Player implements Serializable,Comparable<Player> {
    private String name;
    private Score score;

    public Player(String name) {
        this.name = name;
        this.score = new Score();
    }

    public String getName() {
        return name;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }


    @Override
    public int compareTo(Player player) {
        int comparePlayer = ((Player)player).getScore().getResultCorrect();

        //  For Ascending order
        return  comparePlayer - this.getScore().getResultCorrect() ;
    }
}
