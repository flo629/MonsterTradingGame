package org.example.repository;

import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.repository.PackageRepository;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PackageRepositoryTest {

    @Test
    void testSavePackage() throws Exception {
        // Mock ConnectionPool and dependencies
        ConnectionPool connectionPool = mock(ConnectionPool.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Configure behavior for mocked ConnectionPool and Connection
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Instantiate PackageRepository with mocked ConnectionPool
        PackageRepository packageRepository = new PackageRepository(connectionPool);

        Card[] cards = {
                new Card("1", Card.CardName.FireGoblin, Card.CardType.monster, Card.Element.fire, 10),
                new Card("2", Card.CardName.WaterElf, Card.CardType.monster, Card.Element.water, 15),
                new Card("3", Card.CardName.RegularTroll, Card.CardType.monster, Card.Element.regular, 20),
                new Card("4", Card.CardName.Knight, Card.CardType.monster, Card.Element.regular, 25),
                new Card("5", Card.CardName.Dragon, Card.CardType.monster, Card.Element.fire, 30)
        };

        // Assert that savePackage does not throw any exceptions
        assertDoesNotThrow(() -> packageRepository.savePackage("package123", cards));

        // Verify that the expected SQL was executed
        verify(connection).prepareStatement("INSERT INTO packages (package_id, card1_id2, card2_id2, card3_id2, card4_id2, card5_id2) VALUES (?, ?, ?, ?, ?, ?)");
        verify(preparedStatement).setString(1, "package123");
        verify(preparedStatement).setString(2, "1");
        verify(preparedStatement).setString(3, "2");
        verify(preparedStatement).setString(4, "3");
        verify(preparedStatement).setString(5, "4");
        verify(preparedStatement).setString(6, "5");
        verify(preparedStatement).execute();
    }



}
