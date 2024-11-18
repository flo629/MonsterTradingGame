package org.example.application.MonsterGame.controller;

import org.example.application.MonsterGame.entity.User;
import org.example.application.MonsterGame.service.UserService;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

public class UserController {
    private final UserService userService = new UserService();

    public Response create(Request request) {


        User user = extractFromBody(request.getBody());
        user = userService.create(user);

        Response response = new Response();
        response.setStatus(Status.CREATED);
        response.setHeader("Content-Type", "application/json");
        response.setBody(
                "{ \"studentId\": \"%s\" }"
                        .formatted(user.getId())
        );

        return response;
    }

    private User extractFromBody(String body) {
        String firstName = "";
        String lastName = "";

        String[] lines = body.split("\n");
        for (String line : lines) {
            if (!line.contains(":")) {
                continue;
            }

            String[] keyValue = line.split(":");
            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1]
                    .trim()
                    .replace("\"", "")
                    .replace(",", "");

            if (key.equals("firstName")) {
                firstName = value;
            }
            if (key.equals("lastName")) {
                lastName = value;
            }
        }

        return new User(firstName, lastName);
    }
}
