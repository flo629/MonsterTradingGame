package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Stats;
import org.example.application.monsterGame.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class StatsDbRepository {

    private final ConnectionPool connectionPool;

    public StatsDbRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Optional<Stats> findByUsername(String username) {
        String query = "SELECT username, loses, wins, elo FROM users WHERE username = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, username);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Stats stats = new Stats();
                stats.setUserName(resultSet.getString("username"));
                stats.setTotalLosses(resultSet.getInt("loses"));
                stats.setTotalWins(resultSet.getInt("wins"));
                stats.setElo(resultSet.getInt("elo"));
                return Optional.of(stats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error accessing database", e);
        }
        return Optional.empty();
    }

}
