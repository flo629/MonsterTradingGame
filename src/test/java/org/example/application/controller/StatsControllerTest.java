package org.example.application.controller;

import org.example.application.monsterGame.controller.StatsController;
import org.example.application.monsterGame.entity.Stats;
import org.example.application.monsterGame.service.StatsService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatsControllerTest {

    @Test
    void testGetStats_Unauthorized() {
        // Arrange
        StatsService statsService = mock(StatsService.class);
        StatsController statsController = new StatsController(statsService);

        String invalidHeader = "InvalidToken";

        // Erstelle ein Request-Objekt und setze die Werte mit den Settern
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/stats");
        request.setHeader("Authorization", invalidHeader);

        // Act
        Response response = statsController.handle(request);

        // Assert
        assertEquals(Status.CONFLICT, response.getStatus());
        assertTrue(response.getBody().contains("Authorization header is incorrect"));
    }

    @Test
    void testGetStats_Success() {
        // Arrange
        StatsService statsService = mock(StatsService.class);
        StatsController statsController = new StatsController(statsService);

        Stats stats = new Stats("example", 10, 5, 1200);
        when(statsService.getStats("example")).thenReturn(stats);

        String authorizationHeader = "Bearer example-mtcgToken";

        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/stats");
        request.setHeader("Authorization", authorizationHeader);

        // Act
        Response response = statsController.handle(request);

        // Assert
        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getBody().contains("\"Username\":\"example\""));
        assertTrue(response.getBody().contains("\"wins\":10"));
        assertTrue(response.getBody().contains("\"loses\":5"));
        assertTrue(response.getBody().contains("\"elo\":1200"));
    }

}
