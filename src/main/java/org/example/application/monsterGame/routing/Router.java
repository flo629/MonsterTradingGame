package org.example.application.monsterGame.routing;

import org.example.application.monsterGame.controller.*;
import org.example.application.monsterGame.exception.ControllerNotFound;

import java.util.ArrayList;
import java.util.List;

public class Router {

    private final List<Route> routes;

    public Router() {
        routes = new ArrayList<>();
    }

    public Controller getController(String path){

        for(Route route : this.routes){
            if(!path.startsWith(route.getRoute())){
                continue;
            }
            return route.getController();
     }
        throw new ControllerNotFound(path);
    }

    public void addRoute(String route, Controller controller){
        this.routes.add(new Route(route, controller));
    }

}
