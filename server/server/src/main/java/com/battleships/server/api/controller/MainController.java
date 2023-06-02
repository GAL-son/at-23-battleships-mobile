package com.battleships.server.api.controller;

import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.battleships.server.api.Exceptions.InvalidPasswordException;
import com.battleships.server.api.Exceptions.NoUserException;
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
    
    @PostMapping(path = "/api/login")
    public User loginUser(@RequestParam String login, @RequestParam String password)
    {
        User user = null;
        user = userService.loginUser(login, password);
        return user;
    }

    @PostMapping(path = "/api/logout")
    public Boolean logoutUser(@RequestParam String login, @RequestParam String password) {
        return userService.logout(login, password);
    }

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

    @GetMapping("/api/ranking")
    public List<User> getRanking() {
        return userService.getUsers();
    }



    @RequestMapping("/test")
    public String test()
    {
        return "LMAO DZIAŁA?";
    }

}
