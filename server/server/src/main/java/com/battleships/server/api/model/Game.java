package com.battleships.server.api.model;

import java.util.List;
import java.util.Random;

public class Game {
    private int gameId;

    // Game data
    private int turn; // 0 - P1, 1 - P2
    private int boardSize;
    private boolean gameStarted;

    // Player 1 data
    private User player1;
    private List<Ship> p1Ships;
    private Ship[][] p1Board; 
    private int p1FieldsAlive;
    
    // Player 2 data
    private User player2;
    private List<Ship> p2Ships;
    private Ship[][] p2Board;
    private int p2FieldsAlive;

    
    // Constructor
    public Game(int gid, int boardSize) {
        this.gameId = gid;
        this.boardSize = boardSize;

        gameStarted = false;

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
       
        player1 = null;
        player2 = null;

        // Set number of ships
        p1FieldsAlive = 0;
        p2FieldsAlive = 0;
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

    // Game Functions
    public void playerJoin(User user) throws Exception {
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
            throw new Exception("INVALID SHIP SIZE");
        }

        Ship ship = new Ship(shipSize);

        int player = getPlayerFromPid(pid);        

        if(player == 0){
            p1Ships.add(ship);
            p1FieldsAlive += shipSize;
            for(Field f : shipFields) {
                if(!isFieldCorrect(f)) throw new Exception("INVALID FIELD");
                p1Board[f.x][f.y] = ship;
            }
        }

        if(player == 1){
            p2Ships.add(ship);
            p2FieldsAlive += shipSize;
            for(Field f : shipFields) {
                if(!isFieldCorrect(f)) throw new Exception("INVALID FIELD");
                p2Board[f.x][f.y] = ship;
            } 
        }
    }

    public Boolean makeMove(int pid, Field move) throws Exception {
        if(!isFieldCorrect(move)) return null;

        int player = getPlayerFromPid(pid);
        boolean moveResult = false;

        if(player == 0) {
            if(!(moveResult = (p2Board[move.x][move.y] == null))) {
                p2Board[move.x][move.y].hit();
            }
            return moveResult;
        }

        if(player == 1) {
            if(!(moveResult = (p1Board[move.x][move.y] == null))) {
                p1Board[move.x][move.y].hit();
            }
            return moveResult;
        }        
        return null;
    }

    public boolean isGameOver()
    {
        return (gameStarted && (p1FieldsAlive == 0 || p2FieldsAlive == 0));
    }

    // Helper functions
    private int getPlayerFromPid(int pid) throws Exception {
        if(pid == player1.getUid()) return 0;
        if(pid == player2.getUid()) return 1;
        throw new Exception("Invalid player");
    }

    private Boolean isFieldCorrect(Field field){
        return ((field.x >= 0 && field.x < boardSize) && (field.y >= 0 && field.y < boardSize));
    }
}