package com.battleships.model.client.players;
import com.battleships.model.client.players.difficulty.Difficulty;


public class PlayerAi extends Player{
    Difficulty difficulty;


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

}
