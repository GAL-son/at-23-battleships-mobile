package client.board;

public class Field {
    private Ship oocupyingShip;
    private boolean wasHit;

    public void hitField() {

    }

    public Boolean isOccupied() {

        return true;

    }

    public Ship getOocupyingShip() {
        return oocupyingShip;
    }

    public boolean isWasHit() {
        return wasHit;
    }

    public void setOocupyingShip(Ship oocupyingShip) {
        this.oocupyingShip = oocupyingShip;
    }

    public void setWasHit(boolean wasHit) {
        this.wasHit = wasHit;
    }
}

