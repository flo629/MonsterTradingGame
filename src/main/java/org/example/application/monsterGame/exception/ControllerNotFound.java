package org.example.application.monsterGame.exception;

public class ControllerNotFound extends RuntimeException {
    public ControllerNotFound(String message) {
        super(message);
    }
}
