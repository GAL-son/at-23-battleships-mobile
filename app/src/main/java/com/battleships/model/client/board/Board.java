package com.battleships.model.client.board;

import java.util.ArrayList;

public class Board {

    private int size=10;
   public ArrayList<ArrayList> fields = new ArrayList<>();

    public Board() {


        for (int m = 0; m <this.size; m++) {
            fields.add(new ArrayList<Field>());
            for (int n = 0; n < this.size; n++) {
                fields.get(m).add(new Field());
            }
        }


    }

    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

}
