package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CardRespository {


    private final ConnectionPool connectionPool;

    public CardRespository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    public void save(Card card){

        String sql = "INSERT INTO cards (id, name, card_damage, type, element) VALUES (?, CAST(? AS card_name), ?, CAST(? AS card_type), CAST(? AS element))";


        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ){
            preparedStatement.setString(1, card.getId());
            preparedStatement.setString(2, card.getName().name());
            preparedStatement.setInt(3, card.getDamage());
            preparedStatement.setString(4, card.getType().name());
            preparedStatement.setString(5, card.getElement().name());
            preparedStatement.execute();

        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
