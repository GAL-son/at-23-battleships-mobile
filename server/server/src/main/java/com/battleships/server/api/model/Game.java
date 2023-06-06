package com.battleships.server.api.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.battleships.server.api.Exceptions.InvalidFieldException;
import com.battleships.server.api.Exceptions.PlayerNotInGameExeption;

public class Game {
    private int gameId;

    // Game data
    private int turn; // 0 - P1, 1 - P2
    private int turnNum = 0;
    private int boardSize;
    private boolean gameStarted;
    private boolean gameFinished;

    // Player 1 data
    private User player1;
    private List<Ship> p1Ships;
    private Ship[][] p1Board; 
    private int p1FieldsAlive;
    private String p1ShipSetup;
    private float p1Score;
    
    // Player 2 data
    private User player2;
    private List<Ship> p2Ships;
    private Ship[][] p2Board;
    private int p2FieldsAlive;
    private String p2ShipSetup;
    private float p2Score;

    // Move History
    private List<Move> history;
        
    // Constructor
    public Game(int gid, int boardSize) {
        this.gameId = gid;
        this.boardSize = boardSize;

        gameStarted = false;
        gameFinished = false;

        // Generate random statring turn
        Random rand = new Random();
        turn = rand.nextInt(2);

        // Generate boards of given size
        p1Board = new Ship[boardSize][boardSize];
        p2Board = new Ship[boardSize][boardSize];

        // Set relevant references to null
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++) {
                p1Board[x][y] = null;
                p2Board[x][y] = null;
            }
        }

        // Create ship lists
        p1Ships = new LinkedList<>();
        p2Ships = new LinkedList<>();
       
        // No players at the begining
        player1 = null;
        player2 = null;

        // Set number of ships
        p1FieldsAlive = 0;
        p2FieldsAlive = 0;

        // Set match score
        p1Score = 0;
        p2Score = 0;

        // Initiate move history
        history = new LinkedList<>();
    }

    // Getters
    public int getGameId() {
        return gameId;
    }

    public int getTurn() {
        return turn;
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public float getP1Score() {
        return p1Score;
    }

    public float getP2Score() {
        return p2Score;
    }

    public int getTurnNum() {
        return turnNum;
    }

    // WHEN CONFLICT TAKE THIS
    public String getPlayerSetup(int uid) {
        int player = getPlayerFromPid(uid);

        if(player == 0) {
            return p1ShipSetup;
        } else {
            return p2ShipSetup;
        }
    }

    public void setPlayerSetup(int uid, String setup) {
        int player = getPlayerFromPid(uid);

        if(player == 0) {
            p1ShipSetup = setup;
        } else {
            p2ShipSetup = setup;
        }
    }

    public Move getLastMove()
    {
        return (history.isEmpty()) ? null : history.get(history.size()-1);
    }

    // Game Functions
    public void playerJoin(User user) throws Exception {
        if(gameStarted || gameFinished) throw new Exception("Game in progress");

        if(player1 == null) {
            player1 = user;
        } else if(player2 == null) {
            player2 = user;
        } else {
            throw new Exception("GAME FULL");
        }
    }

    public boolean isPlayerInGame(User user) {
        return (user.getUid() == player1.getUid() || user.getUid() == player2.getUid()); 
    }

    public void setShip(int pid, int shipSize, List<Field> shipFields) throws Exception {
        if(!(shipSize > 0 && shipSize < 5) || shipFields.size() != shipSize) {
            // TODO: Create invalid ship size exception
            throw new Exception("INVALID SHIP SIZE");
        }

        Ship ship = new Ship(shipSize);
        int player = getPlayerFromPid(pid);        

        // TODO: Create fully functional field checking
        if(player == 0){
            p1Ships.add(ship);
            p1FieldsAlive += shipSize;
            for(Field f : shipFields) {
                if(!isFieldCorrect(f) && p1Board[f.x][f.y] == null) throw new Exception("INVALID FIELD");
                p1Board[f.x][f.y] = ship;
            }
        }

        if(player == 1){
            p2Ships.add(ship);
            p2FieldsAlive += shipSize;
            for(Field f : shipFields) {
                if(!isFieldCorrect(f) && p2Board[f.x][f.y] == null) throw new Exception("INVALID FIELD");
                p2Board[f.x][f.y] = ship;
            } 
        }

        if(p1Ships.size() == 10 && p2Ships.size() == 10) gameStarted = true;
    }

    public Boolean makeMove(Move move){
        if(!gameStarted || gameFinished) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not started or is finished");
        if(!isFieldCorrect(move.getMove())) throw new InvalidFieldException();

        int player = getPlayerFromPid(move.getUid());

        // true - hit, false - missed
        boolean moveResult = false;
        history.add(move);

        if(player == 0) {
            if((moveResult = (p2Board[move.getX()][move.getY()] != null))) {
                p2Board[move.getX()][move.getY()].hit();
                p1Score += 5;
            } else 
            nextTurn();
        } else {
            if((moveResult = (p1Board[move.getX()][move.getY()] != null))) {
                p1Board[move.getX()][move.getY()].hit();
                p2Score += 5;
            } else
            nextTurn();
        }

        turnNum++;
        
        /* DEBUG */ System.out.println("Move: " + move.toJsonObject() + moveResult);

        gameFinished = isGameOver();

        return moveResult;
    }

    public boolean isGameOver() {
        return (gameStarted && (p1FieldsAlive == 0 || p2FieldsAlive == 0));
    }

    public JSONObject getGameStateUpdate(int pid) {
        JSONObject gameState = new JSONObject();
        gameState.put("gid", this.getGameId());
        User player;
        User opponent;

        // Resplve perspective
        if(pid == player1.getUid()) {
            player = player1;
            opponent = player2;
        } else {
            player = player2;
            opponent = player1;
        }

        // Build gamestate JSON
        JSONObject user = new JSONObject();
        user.put("login", player.getLogin());
        user.put("score", player.getGamerScore());
        gameState.put("player", user);

        JSONObject enemy = new JSONObject();
        enemy.put("login",opponent.getLogin());
        enemy.put("score", opponent.getGamerScore());
        gameState.put("opponent", enemy);
        gameState.put("turnId", getTurnPid());
        gameState.put("isStarted", gameStarted);
        gameState.put("isFinished", gameFinished);
        gameState.put("lastMove", (getLastMove() != null) ?  this.getLastMove().toJsonObject() : null);        

        return gameState;
    }

    public int getOpponentPid(int pid) {
        if(pid == player1.getUid()) return player2.getUid();
        if(pid == player2.getUid()) return player1.getUid();
        throw new PlayerNotInGameExeption();
    }

    public int getPlayerPid(int pid) {
        if(pid == player1.getUid()) return player1.getUid();
        if(pid == player2.getUid()) return player2.getUid();
        throw new PlayerNotInGameExeption();
    }

    public void nextTurn()
    {
        turn = (++turn)%2;
    }

    public float getPlayerScore(int uid) {
        if(getPlayerFromPid(uid) == 0) return p1Score;
        else return p2Score;
    }

    public int getWinnerUid() {
        if(!gameFinished)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GAME NOT FINISHED");

        if(p1FieldsAlive <= 0) return player2.getUid();
        else return player1.getUid();
    }

    /* DEBUG */ public void printP1Board()
    {
        for(int y = 0; y < boardSize; y++)
        {
            for(int x = 0; x < boardSize; x++)
            {
                System.out.print((p1Board[x][y] == null) ? "_|" : "X|");
            }
            System.out.println();
        }
        System.out.println();

    }

    /* DEBUG */ public void printP2Board()
    {
        for(int y = 0; y < boardSize; y++)
        {
            for(int x = 0; x < boardSize; x++)
            {
                System.out.print((p2Board[x][y] == null) ? "_|" : "X|");
            }
            System.out.println();
        }
        System.out.println();
    }


    // Helper functions
    private int getPlayerFromPid(int pid){
        if(pid == player1.getUid()) return 0;
        if(pid == player2.getUid()) return 1;
        throw new PlayerNotInGameExeption();
    }
    
    private Boolean isFieldCorrect(Field field){
        return ((field.x >= 0 && field.x < boardSize) && (field.y >= 0 && field.y < boardSize));
    }

    private int getTurnPid()
    {
        if(turn == 0) return player1.getUid();
        else return player2.getUid();
    }
}