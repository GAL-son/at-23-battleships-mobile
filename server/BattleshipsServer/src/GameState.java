import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameState {

    /**
     * InnerGameState
     */
    public record Move(int x, int y) {}

    public int gameID;
    private static int activeGames = 0;

    public int[] PID;
    public int[] shipFields;

    public boolean[][] p1Board;
    public boolean[][] p2Board;

    public int turn; // ID of active plater
    public State state;

    public enum State {
        NONE,
        STARTING,
        SHIP_PLACEMENT,
        RUNNING,
        FINISHED
    }

    public LinkedList<Move> moves;

    GameState() {
        gameID = GameState.activeGames++;
        state = State.STARTING;

        PID = new int[2];
        shipFields = new int[2];
        
        p1Board = new boolean[10][10];
        p2Board = new boolean[10][10];
    }

    public void addPlayers(int p1ID, int p2ID) throws IOException {
        if(state == State.NONE) {
            throw new NullPointerException("GameSate is not initialized");
        }
        if(p1ID == p2ID) {
            throw new IOException("INVALID PLAYER ID'S");
        }

        PID[0] = p1ID;
        PID[1] = p2ID;    
    }

    private void setStartingTurn() {
        Random generator = new Random();
        turn = generator.nextInt(2);
    }

    public void nextTurn()
    {
        turn = (turn++)%2;
    }

    public void setPlayerShips(int PID, List<Move> shipsFields)
    {
        state = State.SHIP_PLACEMENT;
        if(getPlayerIndex(PID) == 0) {
            for(Move move: shipsFields) {
                p1Board[move.x()][move.y()] = true;
            }
        } else {
            for(Move move: shipsFields) {
                p2Board[move.x()][move.y()] = true;
            }
        }
    }

    public void setShipFields(int PID, int numberOfShipFields) {
        int index = getPlayerIndex(PID);
        shipFields[index] = numberOfShipFields;
    }

    private int getPlayerIndex(int PID)
    {
        int index = (PID == this.PID[0]) ? 0 : 1;
        return index;
    }

    public void startGame()
    {
        state = State.RUNNING;
        setStartingTurn();
    }

    public boolean inputMove(int PID, Move move) throws IOException
    {
        if(PID != this.PID[turn]) {
            throw new IOException("ITS NOT YOUR TURN");
        }
        moves.push(move);

        boolean moveResult = false;
        if(getPlayerIndex(PID) == 0){
            moveResult = p1Board[move.x()][move.y()];
        } else {
            moveResult = p2Board[move.x()][move.y()];
        }

        if(moveResult){
            shipFields[(getPlayerIndex(PID)+1)%2]--;
        }

        gameUpdate();
        return moveResult;
    }

    private void gameUpdate()
    {
        nextTurn();
        if(shipFields[0] == 0 || shipFields[1] == 0) {
            state = State.FINISHED;
        }
    }

    public int getWinderPID()
    {
        if(state != State.FINISHED) return 0;
        if(shipFields[0] == 0) return PID[1];
        return PID[0];
    }

    

    




    
    
}
