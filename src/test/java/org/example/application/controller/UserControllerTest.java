package org.example.application.controller;

import org.example.application.monsterGame.controller.UserController;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.service.UserService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Test
    void testCreateUser_Success() {
        // Arrange
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);

        User user = new User();
        user.setUsername("example");
        user.setCoin(20);

        when(userService.create(any(User.class))).thenReturn(user);

        String requestBody = "{\"username\":\"example\"}";
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/users");
        request.setBody(requestBody);

        // Act
        Response response = userController.handle(request);

        // Assert
        assertEquals(Status.CREATED, response.getStatus());
        assertEquals("{\"coin\":20,\"Username\":\"example\",\"Password\":null,\"Name\":null,\"Bio\":null,\"Image\":null}", response.getBody());
    }


    @Test
    void testCreateUser_Conflict() {
        // Arrange
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);

        when(userService.create(any(User.class)))
                .thenThrow(new IllegalArgumentException("username already exists"));

        String requestBody = "{\"username\":\"example\"}";
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/users");
        request.setBody(requestBody);

        // Act
        Response response = userController.handle(request);

        // Assert
        assertEquals(Status.CONFLICT, response.getStatus());
        assertEquals("{\"error\":\"username already exists\"}", response.getBody());
    }

    @Test
    void testLogin_Success() {
        // Arrange
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);

        when(userService.login("example", "password")).thenReturn("example-mtcgToken");

        String requestBody = "{\"Username\":\"example\",\"Password\":\"password\"}";
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/sessions");
        request.setBody(requestBody);

        // Act
        Response response = userController.handle(request);

        // Assert
        assertEquals(Status.OK, response.getStatus());
        assertEquals("{\"token\":\"example-mtcgToken\"}", response.getBody());
    }


    @Test
    void testLogin_Failure() {
        // Arrange
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);

        when(userService.login("example", "wrongpassword"))
                .thenThrow(new IllegalArgumentException("Login failed"));

        String requestBody = "{\"Username\":\"example\",\"Password\":\"wrongpassword\"}";
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setPath("/sessions");
        request.setBody(requestBody);

        // Act
        Response response = userController.handle(request);

        // Assert
        assertEquals(Status.CONFLICT, response.getStatus());
        assertEquals("{\"error\":\"Login failed\"}", response.getBody());
    }


}
