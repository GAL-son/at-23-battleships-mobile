package com.battleships.model.client.board;
import android.util.Log;

import com.battleships.model.client.ship.Ship;

import java.io.Serializable;


public class Field  implements Serializable {
    private Ship oocupyingShip;
    private boolean wasHit;

    public int test;


     public Field()
     {
         oocupyingShip=null;
         wasHit=false;

     }

    public void hitField() {
        this.wasHit=true;
        if( oocupyingShip!=null)
        {
            this.oocupyingShip.hitShip();
            Log.i("ship hit",  "ship was hit, its curent hp is: "+this.oocupyingShip.getHealth());
        }
    }

    public Boolean isOccupied() {

        if(oocupyingShip==null)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Ship getOocupyingShip() {
        return oocupyingShip;
    }

    public boolean getWasHit() {
        return wasHit;
    }

    public void setOocupyingShip(Ship oocupyingShip) {
        this.oocupyingShip = oocupyingShip;
    }

    public void setWasHit(boolean wasHit) {
        this.wasHit = wasHit;
    }

}

