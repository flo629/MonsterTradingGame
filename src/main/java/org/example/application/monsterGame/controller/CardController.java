package org.example.application.monsterGame.controller;

import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.CardService;
import org.example.application.monsterGame.service.StatsService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

public class CardController extends Controller {

    private final CardService cardService;

    public CardController(CardService cardService) {
        super();
        this.cardService = cardService;
    }


    @Override
    public Response handle(Request request) {


        return json(Status.NOT_FOUND, new ErrorResponse("Not Hund"));

    }
}
