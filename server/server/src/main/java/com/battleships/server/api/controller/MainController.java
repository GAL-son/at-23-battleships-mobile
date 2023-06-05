package com.battleships.server.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.battleships.server.api.Exceptions.GameNotFoundExeption;
import com.battleships.server.api.Exceptions.NoUserException;
import com.battleships.server.api.model.Field;
import com.battleships.server.api.model.Game;
import com.battleships.server.api.model.Move;
import com.battleships.server.api.model.User;
import com.battleships.server.service.GameService;
import com.battleships.server.service.UserService;


@RestController
public class MainController {
    
    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public MainController(UserService userService, GameService gameService)
    {
        this.userService = userService;
        this.gameService = gameService;
    }

    // Server related endpoints
    /**
     * Endpoint method used for checking if server is active
     * @return boolean value whether server is active or not
     */
    @GetMapping(path = "/api/server")
    public boolean isServerActive()
    {
        return true;
    }    

    // User related endpoints
    /**
     * Endpoint method user to login to server.
     * @param login - <i>request param</i> - user login
     * @param password - <i>request param</i> - user password of the user
     * @return - data of logged in user
     */
    @PostMapping(path = "/api/login")
    public User loginUser(@RequestParam String login, @RequestParam String password)
    {
        User user = null;
        user = userService.loginUser(login, password);
        return user;
    }

    /**
     * Endpoint method used for loging users out
     * @param login login of user to log out
     * @param password password for authentication
     * @return boolean value wether user was logedout
     */
    @PostMapping(path = "/api/logout")
    public Boolean logoutUser(@RequestParam String login, @RequestParam String password) {
        return userService.logout(login, password);
    }

    /**
     * Endpoint method used for registration of new users
     * @param login - <i>request param<i> - login of new user
     * @param password - <i>request param<i> - password of new user
     * @param email - <i>request param<i> - email of new user
     * @return data of newly created user
     */
    @PostMapping("/api/register")
    public User registerUser(@RequestParam String login, @RequestParam String password, @RequestParam String email)
    {
        System.out.println("REGISTER");
        Optional<String> emailOptional = Optional.empty();
        User user = null;
        if(email.isBlank()) {
            emailOptional = Optional.of("");
        } else {
            emailOptional = Optional.of(email);
        }

        try {
            user = userService.registerUser(login, password, emailOptional);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Endpoint method that returns score of user with given <b>id</b>
     * @param id - <i>request param<i> - id of user that score will be pulled
     * @return score of user with given <b>id</b>
     */
    @GetMapping("/api/stats/{id}")
    public String getScore(@PathVariable("id") int id) {
        System.out.println("USER" + id);
        User user = userService.userRepository.getReferenceById(id);
        if(user == null) {
            throw new NoUserException("No such User");
        }

        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", user.getLogin());
        userJsonObject.put("score", user.getGammerScore());
        
        return userJsonObject.toString();
    }

    /**
     * Endpoint method used for geting player ranking
     * @return List of all users stored in database (id, login, score)
     */
    @GetMapping(path = "/api/ranking")
    public List<User> getRanking() {
        return userService.getUsers();
    }

    // Game related Endpoints
    /**
     * Endpoint method user uses to join game queue
     * @param uid - <i>request param<//i> - id of user joining queue
     * @return boolean value whether queue was joined or not
     */
    @PostMapping(path = "/api/game/join")
    public boolean joinGame(@RequestParam int uid)
    {
        User user = userService.getActiveUser(uid);

        return gameService.enterQueue(user);
    }
    
    /**
     * Endpoint method used to determine if user has been assigned to a game. 
     * @param uid - <i>request param</i> - id of user checking queue
     * @return boolean value whether game has been found or not.
     */
    @GetMapping(path = "/api/game/queue")
    public boolean checkGameQueue(@RequestParam int uid)
    {
        User user = userService.getActiveUser(uid);
        Game game = gameService.updateQueue(user);

        if(game == null) return false;
        else return true;
    }

    /**
     * Endpont method used for seting ships when game is starting
     * @param uid - <i>request param</i> - id of user seting his ships
     * @param shipsJsonString
     * @return
     */
    @PostMapping(path = "/api/game/set")
    public boolean setShips(int uid, String shipsJsonString) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        if(game == null) throw new GameNotFoundExeption("Game doesen't exist");

        JSONObject shipsSetup = new JSONObject(shipsJsonString);
        JSONArray ships = shipsSetup.getJSONArray("ships");

        game.setPlayerSetup(uid, shipsJsonString);
        for(int i = 0; i < ships.length(); i++) {
            JSONObject shipJsonObject = ships.getJSONObject(i);

            int shipSize = shipJsonObject.getInt("size");
            boolean shipVertical = shipJsonObject.getBoolean("vertical");
            JSONArray fieldJsonArray = shipJsonObject.getJSONArray("fieldxy");
            int startx = fieldJsonArray.getInt(0);
            int starty = fieldJsonArray.getInt(1);

            List<Field> fields = new ArrayList<>();
            for(int j = 0; j < shipSize; j++) {
                fields.add(new Field(startx + ((!shipVertical) ? j : 0), starty + ((shipVertical) ? j : 0)));
            }

            try {
                game.setShip(uid, shipSize, fields);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // TODO Create invalid ship exception
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ship", e);
            }
        }

        /* DEBUG */game.printP1Board();
        /* DEBUG */game.printP2Board();

        return false;
    }

    @GetMapping(path = "/api/game/start")
    public String getEnemyBoard(@RequestParam int uid) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        if(game == null) throw new GameNotFoundExeption("Game doesent exist");

        if(game.isGameStarted()) return game.getPlayerSetup(uid);
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GAME NOT STARTED");
    }

    @GetMapping(path = "/api/game/state")
    public String getGameState(int uid) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        if(game == null) throw new GameNotFoundExeption("Game doesent exist");

        // Send gamestate as String
        return game.getGameStateUpdate(uid).toString();
    }  

    @PostMapping(path = "/api/game/move")
    public boolean makeMove(int uid, int x, int y) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        Move move = new Move(uid, x, y);
        return game.makeMove(move);
    }   

}
