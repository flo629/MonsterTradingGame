package org.example.application.controller;

import org.example.application.monsterGame.controller.BattleController;
import org.example.application.monsterGame.service.BattleService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BattleControllerTest {

    @Test
    void testEnqueueForBattle_Success() {
        // Arrange
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(battleService);

        String token = "Bearer example-123";
        String username = "example";

        when(battleService.enqueueForBattle(username)).thenReturn("Battle started!");

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/battles");
        request.setHeader("Authorization", token);

        // Act
        Response response = battleController.handle(request);

        // Assert
        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getBody().contains("Battle started!"));

    }


    @Test
    void testEnqueueForBattle_Unauthorized() {
        // Arrange
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(battleService);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/battles");

        // Act
        Response response = battleController.handle(request);

        // Assert
        assertEquals(Status.CONFLICT, response.getStatus());
        assertTrue(response.getBody().contains("Authorization header is incorrect"));
        verifyNoInteractions(battleService);
    }


    @Test
    void testEnqueueForBattle_RouteNotFound() {
        // Arrange
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(battleService);

        Request request = new Request();
        request.setMethod(Method.GET);
        request.setPath("/invalid");

        // Act
        Response response = battleController.handle(request);

        // Assert
        assertEquals(Status.NOT_FOUND, response.getStatus());
        assertTrue(response.getBody().contains("Route not Found"));
        verifyNoInteractions(battleService);
    }
}
