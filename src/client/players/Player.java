package client.players;

abstract class Player {

    int id;
    Board playerBard;

    public int getId() {
        return id;
    }

    public Board getPlayerBard() {
        return playerBard;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerBard(Board playerBard) {
        this.playerBard = playerBard;
    }


}




