package com.example.talktoe;

public class Users {


    String name;
    String score;

    public Users(){

    }

    public Users(String name){
        this.name = name;
        this.score = "1";

    }



    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(String score) {
        this.score = score;
    }


}
