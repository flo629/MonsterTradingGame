package org.example;

import org.example.application.monsterGame.MonsterGame;
import org.example.server.Server;
import org.example.server.http.Request;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new MonsterGame());
        server.start();
    }
}