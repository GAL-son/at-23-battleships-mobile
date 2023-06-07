package com.battleships.server.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        activeUsers = new LinkedList<User>();
        this.userRepository = userRepository;
        this.activeUsers = new LinkedList<>();
    }

    public User loginUser(String login, String passwd) throws NoUserException, InvalidPasswordException {
        Optional<User> optUser = userRepository.getUserByLogin(login);
        if(optUser.isPresent()) {
            System.out.println(optUser.get().toString());
        }
        //return userRepository.getReferenceById(0);
        if(optUser.isEmpty()) {
            throw new NoUserException("No Such User");
        }
        User u = optUser.get();

        if(passwd.equals(u.getPassword())) {
            activeUsers.add(u);
            /* DEBUG */ System.out.println("USER " + u.getLogin() + " Loged in");
            return u;
        } else {
            throw new InvalidPasswordException("PASSWORD INVALID");
        }
    }

    public User getActiveUser(int id) {
        for (User u : activeUsers) {
            if(u.getUid() == id) return u;
        }
        throw new NoUserException("User not logged in.");
    }

    public User getActiveUser(String login) {
        for (User u : activeUsers) {
            if(u.getLogin().equals(login)) return u;
        }
        throw new NoUserException("User not logged in.");
    }

    public User registerUser(String login, String passwd, Optional<String> email) throws Exception
    {   
        Optional<User> clone = userRepository.getUserByLogin(login);

        if(clone.isPresent()) {
            // TODO: Create Exeption for this
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login already used");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwd);
        user.setEmail(email.isPresent() ? email.get() : "");
        user.setGamerScore(0);

        user = userRepository.save(user);
     
        /* DEBUG */ System.out.println("User registered: " + user);

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
