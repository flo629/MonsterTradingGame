package org.example.application.monsterGame.controller;

import org.example.application.monsterGame.dto.UserDto;
import org.example.application.monsterGame.entity.Stats;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.StatsService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

public class StatsController extends Controller {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        super();
        this.statsService = statsService;
    }

    @Override
    public Response handle(Request request) {

            if(request.getMethod().equals(Method.GET) && request.getPath().equals("/stats")) {
            return getStats(request);
        }
        return json(Status.NOT_FOUND, new ErrorResponse("Not Hund"));

    }


    private Response getStats(Request request) {
        try {
           String header = request.getHeader("Authorization");

           if(header.startsWith("Bearer ")) {
               String token = header.substring("Bearer ".length());

               String username = token.split("-")[0];

               Stats stats = statsService.getStats(username);

               return json(Status.OK, stats);
           }else{

               throw new IllegalArgumentException("Authorization header is incorrect");
           }

        } catch (IllegalArgumentException e) {
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        }
    }
}
