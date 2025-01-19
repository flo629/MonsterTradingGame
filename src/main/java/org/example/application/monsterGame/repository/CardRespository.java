package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    public List<Card> findCardsByUser(String username) {
        String sql = """
            SELECT cards.id, cards.name, cards.type, cards.element, cards.card_damage
            FROM cards
            JOIN ownership ON cards.id = ownership.card_id
            JOIN users ON ownership.owner_id = users.id
            WHERE users.username = ?
              AND ownership.date_end > now()
        """;

        List<Card> cards = new ArrayList<>();
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cards.add(new Card(
                        resultSet.getString("id"),
                        Card.CardName.valueOf(resultSet.getString("name")),
                        Card.CardType.valueOf(resultSet.getString("type")),
                        Card.Element.valueOf(resultSet.getString("element")),
                        resultSet.getInt("card_damage")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving cards for user: " + username, e);
        }

        return cards;
    }

}
