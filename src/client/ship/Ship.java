package client.ship;

public class Ship {
    private int size;
    private int health;
    private String type;//??



    public void hitShip(){
        this.health--;
        //
    }

    public int getSize() {
        return size;
    }

    public int getHealth() {
        return health;
    }

    public String getType() {
        return type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setType(String type) {
        this.type = type;
    }

}
