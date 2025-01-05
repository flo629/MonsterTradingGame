package org.example.application.monsterGame.service;

import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.exception.EntityNotFoundException;
import org.example.application.monsterGame.repository.UserMemoryRepository;
import org.example.application.monsterGame.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserService {


    private final Map<String, String> tokens = new HashMap<>();
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User create(User user) {

        userRepository.findByUsername(user.getUsername())
                        .ifPresent(existingUser -> {
                            throw new IllegalArgumentException("username already exists");
                        });

        user.setId(UUID.randomUUID().toString());
        user.setCoin(20);

        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Login failed");
        }

        userRepository.generateToken(username);

        return user.getUsername() + "-mtcgToken";
    }

    /*
    public User getById(int id) {
        return userRepository.find(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getName(), id));
    }*/
}
