package org.example.application.monsterGame.routing;

import org.example.application.monsterGame.controller.Controller;

public class Route {
    private final String route;

    private final Controller controller;

    public Route(String route, Controller controller) {
        this.route = route;
        this.controller = controller;
    }
    public String getRoute() {
        return route;

    }
    public Controller getController() {
        return controller;
    }
}