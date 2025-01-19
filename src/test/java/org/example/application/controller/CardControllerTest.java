package org.example.application.controller;

import org.example.application.monsterGame.controller.CardController;
import org.example.application.monsterGame.service.CardService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardControllerTest {

    @Test
    void testGetAllCards_NoCards() {
        // Arrange
        CardService cardService = mock(CardService.class);
        CardController cardController = new CardController(cardService);

        when(cardService.getCardsForUser("Player1")).thenReturn(List.of());

        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/cards");
        request.setHeader("Authorization", "Bearer Player1-mtcgToken");

        // Act
        Response response = cardController.handle(request);

        // Assert
        assertEquals(Status.NO_CONTENT, response.getStatus());
        assertEquals("User has no cards", response.getBody().replace("\"", ""));
    }
}
