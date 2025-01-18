package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PackageRepository {


    private final ConnectionPool connectionPool;

    public PackageRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void savePackage(String packageId, Card[] cards){
        System.out.println("Saving package " + packageId);
        String sql = "INSERT INTO packages (package_id, card1_id2, card2_id2, card3_id2, card4_id2, card5_id2) VALUES (?, ?, ?, ?, ?, ?)";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
                ){
            preparedStatement.setString(1, packageId);
            preparedStatement.setString(2, cards[0].getId());
            preparedStatement.setString(3,cards[1].getId());
            preparedStatement.setString(4,cards[2].getId());
            preparedStatement.setString(5,cards[3].getId());
            preparedStatement.setString(6,cards[4].getId());
            preparedStatement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}
