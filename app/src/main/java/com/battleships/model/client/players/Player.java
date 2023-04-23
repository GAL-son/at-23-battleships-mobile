package com.battleships.model.client.players;

import com.battleships.model.client.Move;
import com.battleships.model.client.board.Board;
import com.battleships.model.client.board.Field;


import java.util.ArrayList;

abstract public class Player {

    int id;
    Board playerBoard= new Board();

    public int getId() {
        return id;
    }

    public Board getPlayerBard() {
        return playerBoard;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerBoard (Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Field getField(Move move)
    {
        ArrayList<Field> array =this.playerBoard.fields.get(move.positionX);
        Field field= array.get(move.positionY);

        return field;
    }


}




