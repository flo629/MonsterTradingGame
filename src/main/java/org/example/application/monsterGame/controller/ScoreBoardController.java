package org.example.application.monsterGame.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.monsterGame.entity.Stats;
import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.ScoreBoardService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

import java.util.List;
public class ScoreBoardController extends Controller {

    private final ScoreBoardService scoreBoardService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ScoreBoardController(ScoreBoardService scoreBoardService) {
        this.scoreBoardService = scoreBoardService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET) && request.getPath().equals("/scoreboard")) {
            return getScoreBoard(request);
        }
        return json(Status.NOT_FOUND, new ErrorResponse("Route not found"));
    }

    private Response getScoreBoard(Request request) {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                return json(Status.CONFLICT, new ErrorResponse("Access token is missing or invalid"));
            }


            String token = header.substring("Bearer ".length());
            String username = token.split("-")[0];


            List<Stats> scoreboard = scoreBoardService.getScoreboard();


            return new Response(Status.OK, mapper.writeValueAsString(scoreboard));
        } catch (JsonProcessingException e) {
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse("Error processing response"));
        }
    }
}