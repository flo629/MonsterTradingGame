package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDbRepository implements UserRepository {

    private final static String NEW_STUDENT
            ="INSERT INTO students VALUES(?,?,?)";

    private final ConnectionPool connectionPool;

    public UserDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public User save(User user) {
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(NEW_STUDENT);

        ){
            preparedStatement.setString(1,user.getId());
            preparedStatement.setString(2,user.getUsername());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.execute();

            return user;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public User delete(User user) {
        return null;
    }
}
