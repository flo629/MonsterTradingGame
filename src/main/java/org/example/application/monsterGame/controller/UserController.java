package org.example.application.monsterGame.controller;

import org.example.application.monsterGame.dto.UpdateUserDto;
import org.example.application.monsterGame.dto.UserDto;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.UserService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

import java.util.List;
import java.util.Map;

public class UserController extends Controller {

    private final UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }




    @Override
  public Response handle(Request request) {
      if(request.getMethod().equals(Method.POST) && request.getPath().equals("/users")) {
          return create(request);
      }
      if(request.getMethod().equals(Method.POST) && request.getPath().equals("/sessions")) {
          return login(request);
      }
      if (request.getMethod().equals(Method.PUT) && request.getPath().startsWith("/users/")) {
          return UpdateUser(request);
      }/*
      if(request.getMethod().equals(Method.GET) && request.getPath().equals("/users")) {
          return retrieveUser(request);
      }*/
      return json(Status.NOT_FOUND, new ErrorResponse("Not Found"));
  }



  private Response create(Request request) {
      try {
          User user = fromBody(request.getBody(), User.class);
          user = userService.create(user);

          UserDto userDto = new UserDto();
          userDto.setUsername(user.getUsername());
          userDto.setCoin(user.getCoin());

          return json(Status.CREATED, userDto);
      } catch (IllegalArgumentException e) {
          return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
      }
  }

  private Response login(Request request) {
        try{
            UserDto credentials = fromBody(request.getBody(), UserDto.class);
            String token = userService.login(credentials.getUsername(), credentials.getPassword());

            return json(Status.OK, Map.of("token", token));
        }catch(IllegalArgumentException e){
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        }
  }

    private Response readAll() {
      List<User> users = userService.getAll();

      return json(Status.OK, users);
  }

    private Response UpdateUser(Request request) {
        try {
            String[] pathSegments = request.getPath().split("/");
            if (pathSegments.length != 3) {
                return new Response(Status.NOT_FOUND, "Invalid path");
            }

            String username = pathSegments[2];

            String token = request.getHeader("Authorization");
            if (!userService.checkAuth(username, token)) {
                return new Response(Status.NOT_FOUND, "Invalid token");
            }

            UpdateUserDto userDetails = fromBody(request.getBody(), UpdateUserDto.class);
            userDetails.setUserName(username);

            boolean success = userService.updateUser(userDetails);
            if (success) {
                return new Response(Status.OK, "User updated successfully");
            } else {
                return new Response(Status.NOT_FOUND, "User not found");
            }
        } catch (IllegalArgumentException e) {
            return new Response(Status.NOT_FOUND, e.getMessage());
        }
    }

}
