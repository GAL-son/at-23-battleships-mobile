package com.battleships.model.client.players;

import com.battleships.model.client.Move;
import com.battleships.model.client.board.Board;
import com.battleships.model.client.board.Field;
import com.battleships.model.client.ship.Ship;


import java.io.Serializable;
import java.util.ArrayList;

abstract public class Player implements Serializable {

    int id;
    Board playerBoard= new Board();
    public  ArrayList<ArrayList<Integer>> shipsCoordsAndAlignment=new ArrayList<ArrayList<Integer>>();


    public ArrayList<Ship> ships=new ArrayList<Ship>();
   public ArrayList<Integer> shipsSizes=new ArrayList<Integer>();
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


    public Player(int id) {
        this.id = id;
    }
    public Field getField(Move move)
    {
        ArrayList<Field> array =this.playerBoard.fields.get(move.positionX);
        Field field= array.get(move.positionY);

        return field;
    }


}




