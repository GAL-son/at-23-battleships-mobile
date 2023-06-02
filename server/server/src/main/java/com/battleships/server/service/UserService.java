package com.battleships.server.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.boot.archive.spi.ArchiveContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.battleships.server.api.Exceptions.InvalidPasswordException;
import com.battleships.server.api.Exceptions.NoUserException;
import com.battleships.server.api.model.User;
import com.battleships.server.api.repository.UserRepository;


@Service
public class UserService {
    
    List<User> activeUsers;
    public final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginUser(String login, String passwd) throws NoUserException, InvalidPasswordException {
        Optional<User> optUser = userRepository.getUserByLogin(login);
        if(optUser.isPresent()) {
            System.out.println(optUser.get().toString());
        }
        //return userRepository.getReferenceById(0);
        if(optUser.isEmpty()) {
            throw new NoUserException("NO USER");
        }
        User u = optUser.get();

        if(passwd.equals(u.getPassword())) {
            activeUsers.add(u);
            return u;
        } else {
            throw new InvalidPasswordException("Invalid password");
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

    public Boolean logout(String login, String password)
    {
        for(User u : activeUsers) {
            if(u.getLogin().equals(login) && !u.getPassword().equals(password)) {
                throw new InvalidPasswordException("INVALID PASSWORD");
            }

            if(u.getLogin().equals(login) && u.getPassword().equals(password)) {
                activeUsers.remove(u);
                return true;
            }
        }

        return false;
    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }
    
}