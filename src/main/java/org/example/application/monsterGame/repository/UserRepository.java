package org.example.application.monsterGame.repository;

import org.example.application.monsterGame.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    User delete(User user);



}
