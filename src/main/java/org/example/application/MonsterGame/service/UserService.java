package org.example.application.MonsterGame.service;

import org.example.application.MonsterGame.entity.User;
import org.example.application.MonsterGame.repository.UserMemoryRepository;
import org.example.application.MonsterGame.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserMemoryRepository();
    }

    public User create(User user){

        return userRepository.save(user);
    }
}
