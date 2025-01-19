package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.entity.Deck;
import org.example.application.monsterGame.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BattleRepository {

    private final ConnectionPool connectionPool;

    public BattleRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public User getUserByName(String username) {
        String query = "SELECT id, username, elo FROM users WHERE username = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, username);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setUsername(resultSet.getString("username"));
                user.setElo(resultSet.getInt("elo"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error accessing database", e);
        }
        return null;
    }

    public boolean enqueueUser(User user) {
        String sql = "INSERT INTO queue (id, user_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, java.util.UUID.randomUUID().toString());
            stmt.setString(2, user.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error enqueuing user", e);
        }
    }

    public User getOpponent(User user) {
        String sql = "SELECT user_id FROM queue WHERE user_id != ? LIMIT 1";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getUserById(rs.getString("user_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding opponent", e);
        }
        return null;
    }

    public boolean waitForOpponent(User user, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTime) < timeoutSeconds * 1000) {
            if (getOpponent(user) != null) return false;

            try {
                Thread.sleep(500); // Wartezeit
            } catch (InterruptedException ignored) {
            }
        }
        return true; // Timeout
    }

    public Deck getDeckByUserId(String userId) {
        String sql = """
        SELECT d.card1_id2, d.card2_id2, d.card3_id2, d.card4_id2,
               c1.id AS card1_id, c1.name AS card1_name, c1.type AS card1_type, c1.element AS card1_element, c1.card_damage AS card1_damage,
               c2.id AS card2_id, c2.name AS card2_name, c2.type AS card2_type, c2.element AS card2_element, c2.card_damage AS card2_damage,
               c3.id AS card3_id, c3.name AS card3_name, c3.type AS card3_type, c3.element AS card3_element, c3.card_damage AS card3_damage,
               c4.id AS card4_id, c4.name AS card4_name, c4.type AS card4_type, c4.element AS card4_element, c4.card_damage AS card4_damage
        FROM decks d
        JOIN cards c1 ON d.card1_id2 = c1.id
        JOIN cards c2 ON d.card2_id2 = c2.id
        JOIN cards c3 ON d.card3_id2 = c3.id
        JOIN cards c4 ON d.card4_id2 = c4.id
        WHERE d.user_id = ?
    """;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                List<Card> cards = new ArrayList<>();
                for (int i = 1; i <= 4; i++) {
                    cards.add(new Card(
                            rs.getString("card" + i + "_id"),
                            Card.CardName.valueOf(rs.getString("card" + i + "_name")),
                            Card.CardType.valueOf(rs.getString("card" + i + "_type")),
                            Card.Element.valueOf(rs.getString("card" + i + "_element")),
                            rs.getInt("card" + i + "_damage")
                    ));
                }
                return new Deck(cards);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching deck for user: " + userId, e);
        }
        return null; // Kein Deck gefunden
    }


    public void saveRound(User user1, User user2, Card card1, Card card2, double damage1, double damage2) {
        // Implementiere das Speichern der Battle-Runde
    }

    public void updateStats(String userId, boolean isWinner) {
        System.out.println("Updating stats for user: " + userId + " | Winner: " + isWinner);
        String sql = isWinner
                ? "UPDATE users SET wins = wins + 1 WHERE id = ?"
                : "UPDATE users SET loses = loses + 1 WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating stats for user: " + userId, e);
        }
    }


    public void updateElo(User winner, User loser) {
        String winnerSql = "UPDATE users SET elo = elo + 3 WHERE id = ?";
        String loserSql = "UPDATE users SET elo = elo - 5 WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement winnerStmt = connection.prepareStatement(winnerSql)) {
                winnerStmt.setString(1, winner.getId());
                winnerStmt.executeUpdate();
            }
            try (PreparedStatement loserStmt = connection.prepareStatement(loserSql)) {
                loserStmt.setString(1, loser.getId());
                loserStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating ELO", e);
        }
    }

    private User getUserById(String userId) {
        String query = "SELECT id, username, elo FROM users WHERE id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, userId);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setUsername(resultSet.getString("username"));
                user.setElo(resultSet.getInt("elo"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error accessing database", e);
        }
        return null;
    }

    public void dequeueUser(User user) {
        String sql = "DELETE FROM queue WHERE user_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing user from queue", e);
        }
    }

    public boolean payToWin(User user) {
        String queryCheckCoins = "SELECT coin FROM users WHERE id = ?";
        String queryResetCoins = "UPDATE users SET coin = coin - 10 WHERE id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement checkStatement = connection.prepareStatement(queryCheckCoins)
        ) {
            // Prüfe die aktuellen Münzen des Benutzers
            checkStatement.setString(1, user.getId());
            ResultSet rs = checkStatement.executeQuery();

            if (rs.next() && rs.getInt("coin") > 50) {
                // Münzen auf 0 setzen
                try (PreparedStatement resetStatement = connection.prepareStatement(queryResetCoins)) {
                    resetStatement.setString(1, user.getId());
                    resetStatement.executeUpdate();
                }
                return true; // Booster aktiviert
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error using coin booster for user: " + user.getId(), e);
        }
        return false; //
    }



}
