package com.battleships.model.client;

import android.util.Log;

public class Move {
    public int positionX;
    public int positionY;

    public int type;//0 - shot , 1 - creating

    public Move(int positionX, int positionY, int type) throws  Exception {
       //System.out.println("move on  x:"+positionX+" y:"+positionY);
        Log.i("", "move on  x:"+positionX+" y:"+positionY);
        if(positionX>9 || positionY>9||positionX<0||positionY<0)
            throw new Exception ("move out of boundries");

        this.positionX = positionX;
        this.positionY = positionY;
        this.type = type;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void createMove() {

    }


}
