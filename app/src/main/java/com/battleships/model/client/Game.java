package com.battleships.model.client;
import com.battleships.model.client.players.Player;
import com.battleships.model.client.players.PlayerAi;
import com.battleships.model.client.players.PlayerLocal;
import com.battleships.model.client.players.PlayerRemote;

public class Game {

    private int id;
    private int state;
    // for now 0-in progress, 1-ended , 2- ended with error
    private int type;
    // for now -> with 0 - with ai, 1 - online
    private int turn;



    private Player player1;
    private Player player2;

    public Game(int id,int type)throws Exception {
        //seting stater to in progres
        this.state=0;

        //recoginisng game mode
        System.out.println("witaj w grze w statki");
        this.id=id;
        if(type>=0&&type<=1) {
            this.type=type;
        }
        else{
            this.state=2;
            throw new Exception("type specified not recoginised");
        }

        this.player1=new PlayerLocal();

        //seting up right players
        switch (this.type)
        {
            case 0:
                this.player2= new PlayerAi();
                break;
            case 1:
                this.player2=new PlayerRemote();
                break;
            default:
                this.state=2;
                throw new Exception("type specified not recoginised");
        }


    }


    public void gameLoop()
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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

}
