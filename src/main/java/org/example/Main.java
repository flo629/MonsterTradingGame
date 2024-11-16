package org.example;

import org.example.application.echo.EchoApplication;
import org.example.application.html.SimpleHtmlApplication;
import org.example.server.Server;
import org.example.server.http.Request;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new SimpleHtmlApplication());
        server.start();
    }
}