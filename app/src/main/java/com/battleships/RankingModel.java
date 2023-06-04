package com.battleships;

public class RankingModel {

    String pos;
    String userName;
    String score;

    public RankingModel(String pos, String userName, String score) {
        this.pos = pos;
        this.userName = userName;
        this.score = score;
    }

    public String getPos() {
        return pos;
    }

    public String getUserName() {
        return userName;
    }

    public String getScore() {
        return score;
    }
}
