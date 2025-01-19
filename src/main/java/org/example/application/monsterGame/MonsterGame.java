package org.example.application.monsterGame;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.application.monsterGame.controller.*;
import org.example.application.monsterGame.entity.User;
import org.example.application.monsterGame.exception.ControllerNotFound;
import org.example.application.Data.ConnectionPool;
import org.example.application.monsterGame.repository.*;
import org.example.application.monsterGame.routing.Router;
import org.example.application.monsterGame.service.*;
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
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

        DeckRepository deckRepository = new DeckRepository(connectionPool);
        DeckService deckService = new DeckService(deckRepository);

        ScoreBoardRepository scoreBoardRepository = new ScoreBoardRepository(connectionPool);
        ScoreBoardService scoreBoardService = new ScoreBoardService(scoreBoardRepository);


        this.router.addRoute("/users", new UserController(userService));
        this.router.addRoute("/sessions", new UserController(userService));
        this.router.addRoute("/stats", new StatsController(statsService));
        Controller packageController = new PackageController(packageservice, cardService);
        this.router.addRoute("/packages", packageController);
        this.router.addRoute("/transactions", packageController);
        this.router.addRoute("/cards", new CardController(cardService));
        this.router.addRoute("/deck", new DeckController(deckService));
        this.router.addRoute("/scoreboard", new ScoreBoardController(scoreBoardService));

        System.out.println("Route /users registered");
        System.out.println("Route /sessions registered");






        //this.router.addRoute("/sessions", new UserController());
    }
}
