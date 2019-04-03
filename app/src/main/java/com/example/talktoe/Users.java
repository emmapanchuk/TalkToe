package com.example.talktoe;

public class Users {


    String name;
    int score;

    public Users(String name){
        this.name = name;
        this.score = 0;

    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
