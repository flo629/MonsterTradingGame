package org.example.application.monsterGame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.monsterGame.entity.Deck;
import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.CardService;
import org.example.application.monsterGame.service.DeckService;
import org.example.application.monsterGame.service.Packageservice;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

import java.util.Optional;

public class DeckController extends Controller {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET) && request.getPath().equals("/deck")) {
            return getDeck(request);
        }
        if (request.getMethod().equals(Method.PUT) && request.getPath().equals("/deck")) {
            return configureDeck(request);
        }
        return json(Status.NOT_FOUND, new ErrorResponse("Route does not exist"));
    }

    private Response getDeck(Request request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return json(Status.CONFLICT, new ErrorResponse("Authorization token is missing or invalid"));
        }

        String username = token.substring("Bearer ".length()).split("-")[0];

        Optional<Deck> deck = deckService.getDeckByUsername(username);

        if (deck.isEmpty()) {
            return json(Status.NO_CONTENT, "No deck found for the user");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String deckJson = mapper.writeValueAsString(deck.get());
            return new Response(Status.OK, deckJson);
        } catch (Exception e) {
            return new Response(Status.INTERNAL_SERVER_ERROR, "Error serializing deck");
        }
    }

    private Response configureDeck(Request request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return json(Status.CONFLICT, new ErrorResponse("Authorization token is missing or invalid"));
        }

        String username = token.substring("Bearer ".length()).split("-")[0];

        try {
            ObjectMapper mapper = new ObjectMapper();
            String[] cardIds = mapper.readValue(request.getBody(), String[].class);
            System.out.println(cardIds[0]);

            if (cardIds.length != 4) {
                return json(Status.CONFLICT, new ErrorResponse("A deck must contain exactly 4 cards"));
            }

            deckService.configureDeck(username, cardIds);
            return new Response(Status.OK, "Deck configured successfully");
        } catch (IllegalArgumentException e) {
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return new Response(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
