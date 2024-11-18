package org.example.application.MonsterGame;

import org.example.application.MonsterGame.controller.UserController;
import org.example.application.MonsterGame.entity.User;
import org.example.server.Application;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

public class MonsterGame implements Application {

    private final UserController userController = new UserController();
    @Override
    public Response handle(Request request) {
         if(
                 request.getPath().startsWith("/user")
                 && request.getMethod() == Method.POST
         ){
             return userController.create(request);
         }

         Response response = new Response();
         response.setStatus(Status.NOT_FOUND);

        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{\"error\": \"Path: %s not found\" }"
                        .formatted(request.getPath())
        );

         return response;
    }
}
