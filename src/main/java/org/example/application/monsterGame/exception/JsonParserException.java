package org.example.application.monsterGame.exception;

public class JsonParserException extends RuntimeException {

    public JsonParserException(String message) {
        super(message);
    }

    public JsonParserException(Throwable cause) {
        super(cause);
    }
}