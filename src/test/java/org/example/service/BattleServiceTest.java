package org.example.service;

import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.entity.Deck;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.repository.BattleRepository;
import org.example.application.monsterGame.service.BattleService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleServiceTest {

    @Test
    void testEnqueueForBattle_Success() {
        // Arrange
        BattleRepository battleRepository = mock(BattleRepository.class);
        BattleService battleService = new BattleService(battleRepository);

        User user = new User();
        user.setId("123");
        user.setUsername("example");

        when(battleRepository.getUserByName("example")).thenReturn(user);
        when(battleRepository.enqueueUser(user)).thenReturn(true);

        // Act
        String result = battleService.enqueueForBattle("example");

        // Assert
        assertEquals("Waiting for an opponent...", result);

    }

    @Test
    void testEnqueueForBattle_UserNotFound() {
        // Arrange
        BattleRepository battleRepository = mock(BattleRepository.class);
        BattleService battleService = new BattleService(battleRepository);

        when(battleRepository.getUserByName("nonexistent")).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> battleService.enqueueForBattle("nonexistent"));
        verify(battleRepository, times(1)).getUserByName("nonexistent");
    }

    @Test
    void testPlayBattle_Draw() {
        // Arrange
        BattleRepository battleRepository = mock(BattleRepository.class);
        BattleService battleService = new BattleService(battleRepository);

        User user1 = new User();
        user1.setId("123");
        user1.setUsername("Player1");

        User user2 = new User();
        user2.setId("456");
        user2.setUsername("Player2");

        // Mock users and their decks
        when(battleRepository.getUserByName("Player1")).thenReturn(user1);
        when(battleRepository.enqueueUser(user1)).thenReturn(true);
        when(battleRepository.getOpponent(user1)).thenReturn(user2);

        Deck deck1 = mock(Deck.class);
        Deck deck2 = mock(Deck.class);

        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cards1.add(new Card("Card" + i, Card.CardName.FireElf, Card.CardType.monster, Card.Element.fire, 10));
            cards2.add(new Card("Card" + i, Card.CardName.FireElf, Card.CardType.monster, Card.Element.fire, 10));
        }

        when(battleRepository.getDeckByUserId("123")).thenReturn(deck1);
        when(battleRepository.getDeckByUserId("456")).thenReturn(deck2);
        when(deck1.getCards()).thenReturn(cards1);
        when(deck2.getCards()).thenReturn(cards2);

        // Act
        String result = battleService.enqueueForBattle("Player1");

        // Assert

        assertTrue(result.contains("The battle ends in a draw after 100 rounds."));
    }

    @Test
    void testPlayBattle_Winner() {
        // Arrange
        BattleRepository battleRepository = mock(BattleRepository.class);
        BattleService battleService = new BattleService(battleRepository);

        User user1 = new User();
        user1.setId("123");
        user1.setUsername("Player1");

        User user2 = new User();
        user2.setId("456");
        user2.setUsername("Player2");

        // Mock users and their decks
        when(battleRepository.getUserByName("Player1")).thenReturn(user1);
        when(battleRepository.enqueueUser(user1)).thenReturn(true);
        when(battleRepository.getOpponent(user1)).thenReturn(user2);

        Deck deck1 = mock(Deck.class);
        Deck deck2 = mock(Deck.class);

        // Cards for user1 have higher damage than user2 to ensure user1 wins
        List<Card> cards1 = new ArrayList<>();
        List<Card> cards2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cards1.add(new Card("Card" + i, Card.CardName.FireElf, Card.CardType.monster, Card.Element.fire, 50));
            cards2.add(new Card("Card" + i, Card.CardName.WaterGoblin, Card.CardType.monster, Card.Element.water, 10));
        }

        when(battleRepository.getDeckByUserId("123")).thenReturn(deck1);
        when(battleRepository.getDeckByUserId("456")).thenReturn(deck2);
        when(deck1.getCards()).thenReturn(cards1);
        when(deck2.getCards()).thenReturn(cards2);

        // Act
        String result = battleService.enqueueForBattle("Player1");

        // Assert
        System.out.println("Battle Result: " + result);
        assertTrue(result.contains("Player1 wins the battle!"));
    }



}
