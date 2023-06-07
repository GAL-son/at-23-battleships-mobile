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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.battleships.server.api.Exceptions.GameNotFoundExeption;
import com.battleships.server.api.Exceptions.InvalidPasswordException;
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
     * Endpoint method used for checking if server is active. 
     * <ul>
     * <li>Method - <b>GET</b></li> 
     * <li>Path - {@code /api/server}</li>
     * </ul>
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
     * <ul>
     * <li>Method - <b>POST</b></li> 
     * <li>Path - {@code /api/login}</li>
     * </ul>
     * @param login - <i>request param</i> - user login
     * @param password - <i>request param</i> - user password
     * @return - JSON formatted user data
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
     * <ul>
     * <li>Method - <b>POST</b></li> 
     * <li>Path - {@code /api/logout}</li>
     * </ul>
     * @param login login of user to log out
     * @param password password for authentication
     * @return boolean value whether user was loged out
     */
    @PostMapping(path = "/api/logout")
    public Boolean logoutUser(@RequestParam String login, @RequestParam String password) {
        
        return userService.logout(login, password);
    }

    /**
     * Endpoint method used for registration of new users
     * <ul>
     * <li>Method - <b>POST</b></li> 
     * <li>Path - {@code /api/register}</li>
     * </ul>
     * @param login - <i>request param<i> - login of new user
     * @param password - <i>request param<i> - password of new user
     * @param email - <i>request param<i> - email of new user
     * @return JSON formatted data of newly created user
     */
    @PostMapping("/api/register")
    public User registerUser(@RequestParam String login, @RequestParam String password, @RequestParam String email)
    {
        /* DEBUG */ System.out.println("REGISTER");
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
     * Endpoint method used for changing user password. User must be logged in.
     * @param login - <i>request param<i> - login of user changing password
     * @param oldPassword - <i>request param<i> - old user password
     * @param newPassword - <i>request param<i> - new user password
     * @return Boolean value whether password was changed
     */
    @PostMapping("/api/changePassword")
    public boolean changePassword(String login, String oldPassword, String newPassword) {
        User user = userService.getActiveUser(login);

        if(!user.getPassword().equals(oldPassword)) {
            throw new InvalidPasswordException();
        }

        user.setPassword(newPassword);           
        return userService.userRepository.save(user).getPassword().equals(newPassword);
    }

    /**
     * Endpoint method used for deleting an account
     * <ul>
     * <li>Method - <b>POST</p></li>
     * <li>Path - {@code /api/delete}</li>
     * </ul>
     * @param login - <i>request param<i> - login of deleted user
     * @param password - <i>request param<i> - password of deleted user
     * @return Boolean value whether user was deleted or not
     */
    @PostMapping("/api/delete")
    public boolean deleteAccount(@RequestParam String login, @RequestParam String password) {
        User user = userService.getActiveUser(login);

        if(!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("INVALID PASSWORD");
        }

        userService.logout(login, password);
        userService.userRepository.delete(user);
        
        return (userService.userRepository.findById(user.getUid()).isEmpty());
    }

    // TODO: Decide if nesecary
    /**
     * Endpoint method that returns score of user with given <b>id</b>
     * <ul>
     * <li>Method - <b>GET</b></li> 
     * <li>Path - {@code /api/stats/{uid}}</li>
     * </ul>
     * @param uid - <i>request param<i> - id of user that score will be pulled
     * @return score of user with given <b>id</b>
     */
    @GetMapping("/api/stats/{uid}")
    public String getScore(@PathVariable("uid") int uid) {
        System.out.println("USER" + uid);
        User user = userService.userRepository.getReferenceById(uid);
        if(user == null) {
            throw new NoUserException("No such User");
        }

        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", user.getLogin());
        userJsonObject.put("score", user.getGamerScore());
        
        return userJsonObject.toString();
    }

    /**
     * Endpoint method used for geting player ranking
     * <ul>
     * <li>Method - <b>GET</b></li> 
     * <li>Path - {@code /api/ranking}</li>
     * </ul>
     * @return List of all users stored in database (id, login, score)
     */
    @GetMapping(path = "/api/ranking")
    public List<User> getRanking() {
        return userService.getUsers();
    }

    // Game related Endpoints
    /**
     * Endpoint method used to join game queue
     * <ul>
     * <li>Method - <b>POST</b></li> 
     * <li>Path - {@code /api/game/join}</li>
     * </ul>
     * @param uid - <i>request param</i> - id of user joining queue
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
     * <ul>
     * <li>Method - <b>POST</b></li> 
     * <li>Path - {@code /api/game/queue}</li>
     * </ul>
     * @param uid - <i>request param</i> - id of user checking queue
     * @return boolean value whether game has been found or not.
     */
    @PostMapping(path = "/api/game/queue")
    public boolean checkGameQueue(@RequestParam int uid)
    {
        User user = userService.getActiveUser(uid);
        Game game = gameService.updateQueue(user);

        if(game == null) return false;
        else return true;
    }

    /**
     * Endpont method used for seting ships when game is starting
     * <ul>
     * <li>Method - <b>POST</b></li> 
     * <li>Path - {@code /api/game/set}</li>
     * </ul>
     * @param uid - <i>request param</i> - id of user seting his ships
     * @param shipsJsonString - String containing JSON representation of ship setup. 
     * Format for seting up ships:
     * <pre>
     * {
     *  "ships": [
     *      {
     *      "size": 4,
     *      "fieldxy": [
     *          0,
     *          0
     *      ],
     *      "vertical": true
     *      }, ... 
     *  ]
     * }
     * </pre>
     * @return boolean value whether ships were set correctly
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
                // TODO Create invalid ship exception
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ship", e);
            }
        }

        /* DEBUG */game.printP1Board();
        /* DEBUG */game.printP2Board();

        return false;
    }

    /**
     * Endpoint method for getting ships of player's opponent. 
     * A bit hacky because ldgxeyea wanted this.
     * <ul>
     * <li>Method - <b>GET</b></li> 
     * <li>Path - {@code /api/game/start}</li>
     * </ul>
     * @param uid - <i>request param</i> - id of user seting his ships
     * @return JSON formatted ship setup 
     * @see{ setPlayerShips()}
     */
    @GetMapping(path = "/api/game/start")
    public String getEnemyBoard(@RequestParam int uid) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        if(game == null) throw new GameNotFoundExeption("Game doesent exist");

        // WHEN CONFLICT TAKE THIS
        if(game.isGameStarted()) return game.getPlayerSetup(game.getOpponentPid(uid));
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GAME NOT STARTED");
    }

    // TODO: Decide how to format javadoc return JSON
    /**
     * Endpoint method used for pooling current game state. 
     * <ul>
     * <li>Method - <b>GET</b></li> 
     * <li>Path - {@code /api/game/state}</li>
     * </ul>
     * @param uid - <i>request param</i> - id of user pooling for state
     * @return JSON formatted state of current game:
     * <pre>
     * {
     *  "turnId":1,
     *  "gid":0,
     *  "opponent":
     *      {
     *      "score":0,
     *      "login":"gal_son"
     *      },
     *  "isStarted":true,
     *  "isFinished":false,
     *  "player":{
     *      "score":0,
     *      "login":"test13"
     *      }
     *  "lastMove":{
     *      "uid": 1,
     *      "field": [0, 0]
     *      }
     *  }
     * <pre>
     */
    @GetMapping(path = "/api/game/state")
    public String getGameState(int uid) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        if(game == null) throw new GameNotFoundExeption("Game doesent exist");

        if(game.isGameOver()) {
            // Update GamerScore
            float score = user.getGamerScore() + game.getPlayerScore(uid);
            if(game.getWinnerUid() == user.getUid()) {
                score += 50;

                for(int i = game.getTurnNum(); i <= 200; i++)
                {
                    score += 0.1;
                }
            }

            user.setGamerScore(score);
            userService.userRepository.save(user);
            gameService.endGame(user);
        }

        // Send gamestate as String
        return game.getGameStateUpdate(uid).toString();
    }  

    /**
     * Endpoint method used for making a move when game is started
     * <ul>
     * <li>Method - <b>POST</b></li> 
     * <li>Path - {@code /api/game/move}</li>
     * </ul>
     * @param uid - <i>request param</i> - id of user that is making a move 
     * @param x - <i>request param</i> - x position of move (0-9)
     * @param y - <i>request param</i> - y position of move (0-9)
     * @return boolean value whether move (shot) was succesfull or not (enemy ship was hit)
     */
    @PostMapping(path = "/api/game/move")
    public boolean makeMove(int uid, int x, int y) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        Move move = new Move(uid, x, y);
        return game.makeMove(move);
    }   

    /**
     * Endpoint method used for leaving the game
     * @param uid - <i>request param</i> - id of user that is making a move 
     * @return boolean value whether the game was left
     */
    @PutMapping(path = "/api/game/leave")
    public boolean leaveGame(int uid) {
        User user = userService.getActiveUser(uid);
        Game game = gameService.getPlayerGame(user);

        if(game == null) throw new GameNotFoundExeption("Game doesent exist");

        game.giveUp(uid);
        if(game.isGameOver()) {
            // Update GamerScore
            float score = user.getGamerScore() - 5;

            user.setGamerScore(score);
            userService.userRepository.save(user);
            gameService.endGame(user);
        }
        
        return true;
    }

    //TODO: Delete this later - method for testing
    @PostMapping(value="path")
    public float postMethodName(@RequestParam int uid, @RequestParam float score) {
        User u = userService.userRepository.getReferenceById(uid);
        u.setGamerScore(score);

        userService.userRepository.save(u);
        
        return u.getGamerScore();
    }
    

}
