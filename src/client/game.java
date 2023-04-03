package client;
import client.players.*;

public class game {

    private int id;
    private int state;
    private int type;
    private int turn;



    public void startGame()
    {

    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public int getType() {
        return type;
    }

    public int getTurn() {
        return turn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

}
