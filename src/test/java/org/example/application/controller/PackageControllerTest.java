package org.example.application.controller;

import org.example.application.monsterGame.controller.PackageController;
import org.example.application.monsterGame.service.CardService;
import org.example.application.monsterGame.service.Packageservice;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PackageControllerTest {

    @Test
    void testCreatePackage_AsAdmin_Success() {
        // Arrange
        Packageservice packageService = mock(Packageservice.class);
        CardService cardService = mock(CardService.class);
        PackageController packageController = new PackageController(packageService, cardService);


        String requestBody = "[{\"Id\":\"1\",\"Name\":\"FireGoblin\",\"Damage\": 10.0},{\"Id\":\"2\",\"Name\":\"WaterElf\",\"Damage\": 10.0}]";
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/packages");
        request.setBody(requestBody);
        request.setHeader("Authorization", "Bearer admin-mtcgToken");

        // Act
        Response response = packageController.handle(request);

        // Assert
        assertEquals(Status.OK, response.getStatus());
        assertTrue(response.getBody().contains("created"));
    }

    @Test
    void testCreatePackage_AsNonAdmin_Forbidden() {
        // Arrange
        Packageservice packageService = mock(Packageservice.class);
        CardService cardService = mock(CardService.class);
        PackageController packageController = new PackageController(packageService, cardService);

        String requestBody = "[{\"Id\":\"1\",\"Name\":\"FireGoblin\",\"Damage\": 10.0}]";
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/packages");
        request.setBody(requestBody);
        request.setHeader("Authorization", "Bearer user-mtcgToken");

        // Act
        Response response = packageController.handle(request);

        // Assert
        assertEquals(Status.CONFLICT, response.getStatus());
        assertTrue(response.getBody().contains("Only Admin users are allowed"));
    }

    @Test
    void testAcquirePackage_Success() {
        // Arrange
        Packageservice packageService = mock(Packageservice.class);
        CardService cardService = mock(CardService.class);
        PackageController packageController = new PackageController(packageService, cardService);

        when(packageService.getUserCoins("user")).thenReturn(10);
        when(packageService.getAvailablePackageId()).thenReturn("package123");

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/transactions/packages");
        request.setHeader("Authorization", "Bearer user-mtcgToken");

        // Act
        Response response = packageController.handle(request);

        // Assert
        assertEquals(Status.CREATED, response.getStatus());
        assertTrue(response.getBody().contains("user bought package"));
    }


    @Test
    void testAcquirePackage_InsufficientCoins() {
        // Arrange
        Packageservice packageService = mock(Packageservice.class);
        CardService cardService = mock(CardService.class);
        PackageController packageController = new PackageController(packageService, cardService);

        when(packageService.getUserCoins("user")).thenReturn(3);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/transactions/packages");
        request.setHeader("Authorization", "Bearer user-mtcgToken");

        // Act
        Response response = packageController.handle(request);

        // Assert
        assertEquals(Status.CONFLICT, response.getStatus());
        assertTrue(response.getBody().contains("Not enough coins to buy a package"));
    }


    @Test
    void testAcquirePackage_NoPackagesAvailable() {
        // Arrange
        Packageservice packageService = mock(Packageservice.class);
        CardService cardService = mock(CardService.class);
        PackageController packageController = new PackageController(packageService, cardService);

        when(packageService.getUserCoins("user")).thenReturn(10);
        when(packageService.getAvailablePackageId()).thenReturn(null);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/transactions/packages");
        request.setHeader("Authorization", "Bearer user-mtcgToken");

        // Act
        Response response = packageController.handle(request);

        // Assert
        assertEquals(Status.NOT_FOUND, response.getStatus());
        assertTrue(response.getBody().contains("No available package"));
    }






}
