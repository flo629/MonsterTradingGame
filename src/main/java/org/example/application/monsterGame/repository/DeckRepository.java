package org.example.application.monsterGame.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.entity.Deck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DeckRepository {

    private final ConnectionPool connectionPool;

    public DeckRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Optional<Deck> getDeckByUsername(String username) {
        String sql = """
            SELECT * FROM decks
            WHERE user_id = (SELECT id FROM users WHERE username = ?)
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                List<Card> cards = new ArrayList<>();
                for (int i = 1; i <= 4; i++) {
                    String cardId = resultSet.getString("card" + i + "_id2");
                    if (cardId != null) {
                        cards.add(getCardById(cardId));
                    }
                }
                return Optional.of(new Deck(cards));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching deck for user: " + username, e);
        }

        return Optional.empty();
    }
    public void saveDeck(String username, Deck deck) {
        String sql = """
        INSERT INTO decks (deck_id, card1_id2, card2_id2, card3_id2, card4_id2, user_id)
        VALUES (?, ?, ?, ?, ?, (SELECT id FROM users WHERE username = ?))
        ON CONFLICT (user_id) DO UPDATE
        SET card1_id2 = EXCLUDED.card1_id2,
            card2_id2 = EXCLUDED.card2_id2,
            card3_id2 = EXCLUDED.card3_id2,
            card4_id2 = EXCLUDED.card4_id2
    """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            // Debugging-Informationen anzeigen
            String deckId = UUID.randomUUID().toString();
            System.out.println("Saving deck for user: " + username);
            System.out.println("Deck ID: " + deckId);
            System.out.println("Cards: " + deck.getCards().stream().map(Card::getId).toList());

            // Werte setzen
            preparedStatement.setString(1, deckId); // Eindeutige Deck-ID
            preparedStatement.setString(2, deck.getCards().get(0).getId()); // Karte 1
            preparedStatement.setString(3, deck.getCards().get(1).getId()); // Karte 2
            preparedStatement.setString(4, deck.getCards().get(2).getId()); // Karte 3
            preparedStatement.setString(5, deck.getCards().get(3).getId()); // Karte 4
            preparedStatement.setString(6, username); // Benutzername

            // SQL ausführen
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deck saved successfully!");
            } else {
                System.out.println("Deck save operation affected no rows.");
            }
        } catch (SQLException e) {
            // Fehlerprotokollierung
            e.printStackTrace();
            throw new RuntimeException("Error saving deck for user: " + username, e);
        }
    }


    public Optional<Card> getCardByIdAndUsername(String cardId, String username) {
        String sql = """
            SELECT cards.id, cards.name, cards.type, cards.element, cards.card_damage
            FROM cards
            JOIN ownership ON cards.id = ownership.card_id
            JOIN users ON ownership.owner_id = users.id
            WHERE cards.id = ? AND users.username = ? AND ownership.date_end > NOW()
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, cardId);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Card(
                        resultSet.getString("id"),
                        Card.CardName.valueOf(resultSet.getString("name")),
                        Card.CardType.valueOf(resultSet.getString("type")),
                        Card.Element.valueOf(resultSet.getString("element")),
                        resultSet.getInt("card_damage")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching card: " + cardId, e);
        }

        return Optional.empty();
    }

    public Card getCardById(String cardId) {
        String sql = """
        SELECT id, name, type, element, card_damage
        FROM cards
        WHERE id = ?
    """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Card(
                        resultSet.getString("id"),
                        Card.CardName.valueOf(resultSet.getString("name")),
                        Card.CardType.valueOf(resultSet.getString("type")),
                        Card.Element.valueOf(resultSet.getString("element")),
                        resultSet.getInt("card_damage")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching card with ID: " + cardId, e);
        }

        throw new IllegalArgumentException("Card with ID " + cardId + " does not exist");
    }

}
