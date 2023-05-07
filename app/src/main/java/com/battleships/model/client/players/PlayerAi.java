package com.battleships.model.client.players;
import com.battleships.model.client.players.difficulty.Difficulty;

import java.io.Serializable;


public class PlayerAi extends Player implements Serializable {
    Difficulty difficulty;


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

}
