package com.battleships.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.battleships.server.api.model.User;
import com.battleships.server.api.repository.UserRepository;


@Service
public class UserService {
    
    List<User> activeUsers;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginUser(String login, String passwd) throws Exception {
        Optional<User> optUser = userRepository.getUserByLogin(login);
        //return userRepository.getReferenceById(0);
        if(optUser.isEmpty()) {
            throw new Exception("NO USER");
        }

        if(passwd == optUser.get().getPassword()) {
            return optUser.get();
        } else {
            throw new Exception("Invalid password");
        }
  
    }

    public User registerUser(String login, String passwd, Optional<String> email) throws Exception
    {   
        Optional<User> clone = userRepository.getUserByLogin(login);

        if(clone.isPresent()) {
            throw new Exception("Login Taken");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwd);
        user.setEmail(email.isPresent() ? email.get() : "");
        user.setGammerScore(0);

        user = userRepository.save(user);
     
        return user;
    }

    
}
