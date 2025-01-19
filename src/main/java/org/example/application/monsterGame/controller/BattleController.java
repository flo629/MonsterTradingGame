package org.example.application.monsterGame.controller;

import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.BattleService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

public class BattleController extends Controller {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST) && request.getPath().equals("/battles")) {
            return enqueueForBattle(request);
        }
        return json(Status.NOT_FOUND, new ErrorResponse("Route not Found"));
    }

    private Response enqueueForBattle(Request request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return json(Status.CONFLICT, new ErrorResponse("Authorization header is incorrect"));
        }

        String token = header.substring("Bearer ".length());
        String username = token.split("-")[0];

        try {
            String result = battleService.enqueueForBattle(username);
            return json(Status.OK, result);
        } catch (IllegalArgumentException e) {
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        }
    }
}
