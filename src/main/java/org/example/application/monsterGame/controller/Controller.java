package org.example.application.monsterGame.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.monsterGame.exception.InvalidBodyException;
import org.example.application.monsterGame.exception.JsonParserException;
import org.example.server.http.Status;
import org.example.server.http.Request;
import org.example.server.http.Response;

public abstract class Controller {

    private final ObjectMapper objectMapper;

    public Controller(){
        this.objectMapper = new ObjectMapper();
    }
    public abstract Response handle(Request request);

    protected <T> T fromBody(String body, Class<T> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException(e);
        }
    }

    protected Response json(Status status, Object object) {
        Response response = new Response();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        try {
            response.setBody(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new JsonParserException(e);
        }
        return response;
    }


}
