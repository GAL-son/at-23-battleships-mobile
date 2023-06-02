package com.battleships.server.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.battleships.server.api.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{
    public Optional<User> getUserByLogin(String login);
    public List<User> findAll();
}
