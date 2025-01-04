package org.example.application.monsterGame;

import org.example.application.monsterGame.controller.Controller;
import org.example.application.monsterGame.controller.UserController;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.exception.ControllerNotFound;
import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.repository.UserDbRepository;
import org.example.application.monsterGame.repository.UserMemoryRepository;
import org.example.application.monsterGame.repository.UserRepository;
import org.example.application.monsterGame.routing.Router;
import org.example.application.monsterGame.service.UserService;
import org.example.server.Application;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;


public class MonsterGame implements Application {



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
        ConnectionPool connectionPool = new ConnectionPool();

        UserRepository userRepository = new UserDbRepository(connectionPool);
        UserService userService =  new UserService(userRepository);

        this.router.addRoute("/users", new UserController(userService));
        this.router.addRoute("/sessions", new UserController(userService));
        System.out.println("Route /users registered");
        System.out.println("Route /sessions registered");






        //this.router.addRoute("/sessions", new UserController());
    }
}
