package org.example.application.monsterGame;

import org.example.application.monsterGame.controller.Controller;
import org.example.application.monsterGame.controller.UserController;
import org.example.application.monsterGame.exception.ControllerNotFound;
import org.example.application.monsterGame.routing.Router;
import org.example.server.Application;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

public class MonsterGame implements Application {

    private final UserController userController = new UserController();

    private final Router router;

    public MonsterGame() {
        this.router = new Router();

        this.initializeRoutes();
    }

    @Override
    public Response handle(Request request) {

        try {
            Controller controller
                    = this.router.getController(request.getPath());
            return controller.handle(request);

        } catch (ControllerNotFound e) {
            Response response = new Response();
            response.setStatus(Status.NOT_FOUND);

            response.setHeader("Content-Type", "application/json");
            response.setBody(
                    "{\"error\": \"Path: %s not found\" }"
                            .formatted(e.getMessage())
            );

            return response;
        }
    }

    private void initializeRoutes() {
        this.router.addRoute("/users", new UserController());
        this.router.addRoute("/sessions", new UserController());
    }
}
