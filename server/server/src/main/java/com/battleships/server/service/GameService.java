package com.battleships.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.battleships.server.api.model.Game;
import com.battleships.server.api.model.User;

@Service
public class GameService {
    List<Game> activeGames;
    List<User> userQueue;

    public void addGame(int boardSize)
    {
        activeGames.add(new Game(activeGames.size(), boardSize));
    }

    public void enterQueue(User user)
    {
        if(userQueue.size() == 0) {
            userQueue.add(user);
            return;
        }

        User opponent;
        Float scoreDifference = null;
        for(User u : userQueue)
        {   
            float newScoreDifference = Math.abs(user.getGammerScore() - u.getGammerScore()); 
            if(scoreDifference == null || scoreDifference > newScoreDifference)
            {
                scoreDifference = newScoreDifference;
                opponent = u;
            }
        }

        
    }




}
