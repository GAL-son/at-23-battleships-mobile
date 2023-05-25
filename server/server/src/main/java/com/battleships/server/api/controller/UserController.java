package com.battleships.server.api.controller;

import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.battleships.server.api.model.User;
import com.battleships.server.service.UserService;


@RestController
public class UserController {
    
    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    
    @PostMapping(path = "/api/login")
    public User loginUser(@RequestParam String login, @RequestParam String password)
    {
        User user = null;
        try {
            user = userService.loginUser(login, password);
        } catch (Exception e) {
            
        } 

        return user;
    }

    @PutMapping("/api/register")
    public String registerUser(@RequestParam String login, @RequestParam String password, @RequestParam String email)
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
            userService.registerUser(login, password, emailOptional);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/test")
    public String test()
    {
        return "LMAO DZIAŁA?";
    }

}
