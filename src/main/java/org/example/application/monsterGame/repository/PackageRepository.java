package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    public int getUserCoins(String username){
        String sql = "SELECT coin FROM users WHERE username = ?";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ){
            preparedStatement.setString(1, username);
            var resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("coin");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return 0;
    }

    public String findAvailablePackageId(){
        String sql = "Select package_id FROM packages WHERE is_availabe= true LIMIT 1";
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ){
            var reultSet = preparedStatement.executeQuery();
            if(reultSet.next()){
                return reultSet.getString("package_id");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Card> getCardsFromPackage(String packageId) {
        String sql = """
        SELECT cards.id, cards.name, cards.type, cards.element, cards.card_damage
        FROM cards
        JOIN packages ON packages.card1_id2 = cards.id
            OR packages.card2_id2 = cards.id
            OR packages.card3_id2 = cards.id
            OR packages.card4_id2 = cards.id
            OR packages.card5_id2 = cards.id
        WHERE packages.package_id = ?
    """;
        List<Card> cards = new ArrayList<>();
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, packageId);
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

            throw new RuntimeException(e);
        }
        return cards;
    }

    public void updatePackageAvailability(String packageId, boolean isAvailable) {
        String sql = "UPDATE packages SET is_availabe = ? WHERE package_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setString(2, packageId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public void addOwnership(String cardId, String username) {
        String sql = """
        INSERT INTO ownership (owner_id, date_start, card_id)
        VALUES (
            (SELECT id FROM users WHERE username = ?),
            now(),
            ?
        )
    """;
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, cardId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deductCoins(String username, int amount) {
        String sql = "UPDATE users SET coin = coin - ? WHERE username = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
