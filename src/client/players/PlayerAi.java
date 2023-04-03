package client.players;
import  client.players.difficulty.Difficulty;
public class PlayerAi extends Player{
    Difficulty difficulty;


    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

}
