package com.battleships.model.client.players;
import com.battleships.model.client.board.Board;
import com.battleships.model.client.board.Field;
import com.battleships.model.client.players.difficulty.Difficulty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class PlayerAi extends Player implements Serializable {
    Difficulty difficulty;

    public ArrayList<ArrayList<Integer>>pojectionOfBoard=new ArrayList<>();

    public PlayerAi() {
        super();
        for (int m = 0; m < 10; m++) {
            pojectionOfBoard.add(new ArrayList<Integer>());
            for (int n = 0; n < 10; n++) {
                pojectionOfBoard.get(m).add(0);
            }

        }
    }

    public ArrayList<Integer> getAImove(Board board)
    {
        int x,y;
        Random random=new Random();
        ArrayList<Integer> AImove=new ArrayList<>();
        x=random.nextInt(10);
        y=random.nextInt(10);
        while (pojectionOfBoard.get(x).get(y)==1)
        {
            x=random.nextInt(10);
            y=random.nextInt(10);
        }

        pojectionOfBoard.get(x).set(y,1);

        AImove.add(x);
        AImove.add(y);



        return AImove;
    }
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

}
