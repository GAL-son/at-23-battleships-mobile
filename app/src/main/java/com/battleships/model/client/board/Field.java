package com.battleships.model.client.board;
import com.battleships.model.client.ship.Ship;



public class Field {
    private Ship oocupyingShip;
    private boolean wasHit;


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

