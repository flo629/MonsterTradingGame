package org.example.application.monsterGame.repository;

import org.example.application.monsterGame.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserMemoryRepository implements UserRepository {

    private final List<User> users;

    public UserMemoryRepository() {
        this.users = new ArrayList<>();

        save(new User("kienboec", "daniel"));
        save(new User("altenhof", "markus"));
        save(new User("admin", "istrator"));
    }

    @Override
    public User save(User user) {
        user.setId(String.valueOf(this.users.size() + 1));

        this.users.add(user);

        return user;
    }
    @Override
    public List<User> findAll(){
        return this.users;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public User delete(User user){
        return null;
    }

    @Override
    public void generateToken(String username){

    }

    @Override
    public void update(User user){}



}
