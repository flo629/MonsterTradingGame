package org.example.application.monsterGame.service;

import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.exception.EntityNotFoundException;
import org.example.application.monsterGame.repository.UserMemoryRepository;
import org.example.application.monsterGame.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final UserRepository userRepository;
    private final Map<String, String> tokens = new HashMap<>();

    public UserService() {
        this.userRepository = new UserMemoryRepository();
    }

    public User create(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("User already exists");
                });

        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }


        String token = username + "-mtcgToken";
        tokens.put(username, token);

        return token;
    }

    /*
    public User getById(int id) {
        return userRepository.find(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getName(), id));
    }*/
}
