package org.example.application.MonsterGame.repository;

import org.example.application.MonsterGame.entity.User;
import java.util.ArrayList;
import java.util.List;

public class UserMemoryRepository implements UserRepository {

    private final List<User> users;

    public UserMemoryRepository() {
        this.users = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        user.setId(this.users.size() + 1);
        this.users.add(user);

        return user;
    }
}
