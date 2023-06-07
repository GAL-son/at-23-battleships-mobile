package com.battleships.server.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.battleships.server.api.Exceptions.NoUserException;
import com.battleships.server.api.model.Game;
import com.battleships.server.api.model.User;

@Service
public class GameService {
    List<Game> activeGames;
    List<User> userQueue;
    Map<User, Game> playersInGame;

    public GameService() {
        activeGames = new LinkedList<Game>();
        userQueue = new LinkedList<User>();
        playersInGame = new HashMap<User, Game>();
    }
    
    public Game getGame(int activeGameId) {
        for(Game g : activeGames) {
            if(g.getGameId() == activeGameId) return g;
        }
        
        return null;
    }
    
    public boolean enterQueue(User user) {
        userQueue.add(user);
        return userQueue.contains(user);        
    }
    
    public Game updateQueue(User user) {
        // CHECK IF PLAYER ALREADY IN GAME
        if(getPlayerGame(user)!= null) {
            return getPlayerGame(user);
        }

        /* DEBUG */ System.out.println(userQueue);

        // QUEUE IS EMPTY
        if(userQueue.size() == 1) return null;
        
        // FIND OPPONENT
        User opponent = null;
        Float scoreDifference = null;
        for(User u : userQueue)
        {   
            if( u.getUid() == user.getUid()) continue;
            float newScoreDifference = Math.abs(user.getGamerScore() - u.getGamerScore()); 
            if(scoreDifference == null || scoreDifference > newScoreDifference)
            {
                scoreDifference = newScoreDifference;
                opponent = u;
            }
        }

        /* DEBUG */ System.out.println("Opponent found: " + opponent + " withd score difference" + scoreDifference);

        if(opponent == null) throw new NoUserException("Error when finding opponent!!!");
        
        // OPPONENT FOUND - CREATE GAME
        // Find empty game id
        int maxid = 0;
        for(Game g : activeGames) {
            int gid = g.getGameId();
            if(gid > maxid) maxid = gid; 
        }
        
        // Push Game to list
        Game game = new Game(maxid, 10);
        addGame(game);
        
        // Add Players
        try {
            game.playerJoin(user);
            userQueue.remove(user);
            playersInGame.put(user, game);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed adding player");
            e.printStackTrace();
        }
        
        try {
            game.playerJoin(opponent);
            userQueue.remove(opponent);
            playersInGame.put(opponent, game);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed adding opponent");
            e.printStackTrace();
        }

        /* DEBUG */ System.out.println(userQueue);
        
        return game;
    }

    public Game getPlayerGame(User user) {
        for(Map.Entry<User, Game> m : playersInGame.entrySet()) {
            if(user.getUid() == m.getKey().getUid()) return m.getValue();
        }
        return null;
    }

    public void endGame(User user) {
        Game game = playersInGame.get(user);        
        playersInGame.remove(user);

        if(!playersInGame.containsValue(game)) {
            activeGames.remove(game);
        }
    }

    // public boolean makeMove(int gid, User user, Move move){
    //     Game game = this.getGame(gid);
    //     // TODO: Make exceptions
    //     if(game == null) throw new GameNotFoundExeption();
    //     if(!game.isPlayerInGame(user)) throw new PlayerNotInGameExeption();

    //     // TODO: Prolly make exeptionn for this
    //     //game.makeMove(user.getUid(), move);

    //     return false;
    // }
    
    // public boolean isGameStated(int gid) throws Exception {
    //     Game game = this.getGame(gid);
    //     if(game == null) throw new Exception("LMAO NO GAME");

    //     return game.isGameStarted();
    // }

    // Helper functions
    private void addGame(Game game)
    {
        activeGames.add(game);
    }
    
    
}
