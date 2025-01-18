package org.example.application.monsterGame;

import org.example.application.monsterGame.controller.Controller;
import org.example.application.monsterGame.controller.PackageController;
import org.example.application.monsterGame.controller.StatsController;
import org.example.application.monsterGame.controller.UserController;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.exception.ControllerNotFound;
import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.repository.*;
import org.example.application.monsterGame.routing.Router;
import org.example.application.monsterGame.service.CardService;
import org.example.application.monsterGame.service.Packageservice;
import org.example.application.monsterGame.service.StatsService;
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

        StatsDbRepository statsDbRepository = new StatsDbRepository(connectionPool);
        StatsService statsService = new StatsService(statsDbRepository);

        PackageRepository packageRepository = new PackageRepository(connectionPool);
        Packageservice packageservice = new Packageservice(packageRepository);

        CardRespository cardRepository = new CardRespository(connectionPool);
        CardService cardService = new CardService(cardRepository);


        this.router.addRoute("/users", new UserController(userService));
        this.router.addRoute("/sessions", new UserController(userService));
        this.router.addRoute("/stats", new StatsController(statsService));
        this.router.addRoute("/packages", new PackageController(packageservice, cardService));

        System.out.println("Route /users registered");
        System.out.println("Route /sessions registered");






        //this.router.addRoute("/sessions", new UserController());
    }
}
