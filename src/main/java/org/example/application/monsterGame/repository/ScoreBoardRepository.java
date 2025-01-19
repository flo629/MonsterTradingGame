package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreBoardRepository {
    private final ConnectionPool connectionPool;

    public ScoreBoardRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Stats> getScoreboard() {
        String sql = """
            SELECT username, elo, wins, loses
            FROM users
            ORDER BY elo DESC
        """;
        List<Stats> scoreboard = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                scoreboard.add(new Stats(
                        rs.getString("username"),
                        rs.getInt("wins"),
                        rs.getInt("loses"),
                        rs.getInt("elo")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching scoreboard", e);
        }
        return scoreboard;
    }
}
