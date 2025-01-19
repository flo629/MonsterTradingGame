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
import java.util.Optional;

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
      }
      if(request.getMethod().equals(Method.GET) && request.getPath().startsWith("/users/")) {
          return retrieveUser(request);
      }
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
        try{

            String[] pathsegments= request.getPath().split("/");

            if(pathsegments.length != 3) {
                throw new IllegalArgumentException("Invalid path");
            }

            String userName =pathsegments[2];

            if(userName.isEmpty()){
                throw new IllegalArgumentException("No username provided");
            }

            String token = request.getHeader("Authorization");

            if(!userService.checkAuth(userName,token)){
                throw new IllegalArgumentException("Invalid token");
            }

            UpdateUserDto credentials =  fromBody(request.getBody(), UpdateUserDto.class);
            credentials.setUserName(userName);

            boolean success = userService.updateUser(credentials);

            if(success){
                return json(Status.OK, credentials);
            }

            return json(Status.CONFLICT, new ErrorResponse("User Not found"));


        }catch(IllegalArgumentException e){
            return json(Status.CONFLICT,new ErrorResponse(e.getMessage()));
        }
  }

    private Response retrieveUser(Request request) {
        try {
            // Parse the path to get the username
            String[] pathsegments = request.getPath().split("/");
            if (pathsegments.length != 3) {
                throw new IllegalArgumentException("Invalid path");
            }

            String username = pathsegments[2];
            if (username.isEmpty()) {
                throw new IllegalArgumentException("No username provided");
            }

            // Validate the Authorization token
            String token = request.getHeader("Authorization");
            if (!userService.checkAuth(username, token)) {
                return json(Status.CONFLICT, new ErrorResponse("Unauthorized"));
            }

            // Retrieve the user data
            Optional<User> userOpt = userService.retrieveUser(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Return only bio, name, and image
                Map<String, String> response = Map.of(
                        "Name", user.getName(),
                        "Bio", user.getBio(),
                        "Image", user.getImage()
                );

                return json(Status.OK, response);
            } else {
                return json(Status.NOT_FOUND, new ErrorResponse("User not found"));
            }
        } catch (IllegalArgumentException e) {
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        }
    }






}
