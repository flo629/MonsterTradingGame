package org.example.application.monsterGame.controller;

import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.service.UserService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

import java.util.List;

public class UserController extends Controller {

    private final UserService userService = new UserService();

    @Override
  public Response handle(Request request) {
      if(request.getMethod().equals(Method.POST) && request.getPath().equals("/user")) {
          return create(request);
      }
      if(request.getMethod().equals(Method.GET)) {
          return readAll();
      }
        if (request.getMethod().equals(Method.POST) && request.getPath().equals("/sessions")) {
            return login(request);
        }
      return null;
  }



  private Response create(Request request) {
      try {
          User user = fromBody(request.getBody(), User.class);
          user = userService.create(user);

          return json(Status.CREATED, user);
      } catch (IllegalArgumentException e) {
          return json(Status.NOT_FOUND, e.getMessage());
      }
  }

    private Response login(Request request) {
        try {
            User credentials = fromBody(request.getBody(), User.class);


            String token = userService.login(credentials.getUsername(), credentials.getPassword());

            return json(Status.OK, "{\"token\":\"" + token + "\"}");
        } catch (IllegalArgumentException e) {
            return json(Status.NOT_FOUND, "{\"error\":\"Login failed: " + e.getMessage() + "\"}");
        }
    }


    private Response readAll() {
      List<User> users = userService.getAll();

      return json(Status.OK, users);
  }

}
