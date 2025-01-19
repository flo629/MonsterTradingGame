package org.example.application.monsterGame.controller;

import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.CardService;
import org.example.application.monsterGame.service.StatsService;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

import java.util.List;
import java.util.Map;

public class CardController extends Controller {

    private final CardService cardService;

    public CardController(CardService cardService) {
        super();
        this.cardService = cardService;
    }


    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET) && request.getPath().equals("/cards")) {
            return getAllCards(request);
        }
        return json(Status.NOT_FOUND, new ErrorResponse("Route does not exist"));
    }

    private Response getAllCards(Request request) {
        try {
            String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                return json(Status.CONFLICT, new ErrorResponse("Authorization token is missing or invalid"));
            }

            String token = header.substring("Bearer ".length());
            String username = token.split("-")[0];


            List<Card> cards = cardService.getCardsForUser(username);

            if (cards.isEmpty()) {
                return json(Status.NO_CONTENT, "User has no cards");
            }

            List<Map<String, String>> listCards = cards.stream()
                    .map(card -> Map.of(
                            "Id", card.getId(),
                            "Name", card.getName().toString()
                    ))
                    .toList();

            return json(Status.OK, listCards);

        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, new ErrorResponse("An unexpected error occurred"));
        }
    }


}
