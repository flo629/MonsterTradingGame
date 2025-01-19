package org.example.application.monsterGame.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.entity.Stats;
import org.example.application.monsterGame.error.ErrorResponse;
import org.example.application.monsterGame.service.CardService;
import org.example.application.monsterGame.service.Packageservice;
import org.example.server.http.Method;
import org.example.server.http.Request;
import org.example.server.http.Response;
import org.example.server.http.Status;

import java.util.List;

public class PackageController extends Controller{

    private final Packageservice packageService;
    private final CardService cardService;

    public PackageController(Packageservice packageService, CardService cardService) {
        super();
        this.packageService = packageService;
        this.cardService = cardService;
    }

    @Override
    public Response handle(Request request) {

        if(request.getMethod().equals(Method.POST) && request.getPath().equals("/packages")) {
            return createPackage(request);
        }
        if(request.getMethod().equals(Method.POST) && request.getPath().equals("/transactions/packages")) {
            return acquierePackage(request);
        }
        return json(Status.NOT_FOUND, new ErrorResponse("Route does not exist"));

    }

    private Response createPackage(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        Card[] cards;
        try{
            String header = request.getHeader("Authorization");

            if(header.startsWith("Bearer ")) {
                String token = header.substring("Bearer ".length());

                String username = token.split("-")[0];

                if(!username.equals("admin")){
                throw new IllegalArgumentException("Only Admin users are allowed");
                }


                cards = objectMapper.readValue(request.getBody(), Card[].class);

                for (Card card : cards) {
                    card.setType(detectCardType(card.getName()));
                    card.setElement(detectElement(card.getName()));

                    cardService.addCard(card);
                }


                String packageId = packageService.createPackage(cards);

                return json(Status.OK, "created" + packageId);
            }else{
                throw new IllegalArgumentException("Token does not begin with Bearer ");
            }


        }catch(IllegalArgumentException e){
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Response acquierePackage(Request request) {
        try{
            String header = request.getHeader("Authorization");

            if(!header.startsWith("Bearer ")){
                throw new IllegalArgumentException("Wrong Authorization");
            }

            String token = header.substring("Bearer ".length());
            String username = token.split("-")[0];

            int userCoins = packageService.getUserCoins(username);

            if(userCoins < 5){
                return json(Status.CONFLICT, new ErrorResponse("Not enough coins to buy a package"));
            }

            String packageId = packageService.getAvailablePackageId();

            if(packageId == null){
                return json(Status.NOT_FOUND, new ErrorResponse("No available package"));
            }

            packageService.assignPackageToUser(packageId, username);

            return json(Status.CREATED, "user bought package");
        }catch(IllegalArgumentException e){
            return json(Status.CONFLICT, new ErrorResponse(e.getMessage()));
        }
    }


    private Card.Element detectElement(Card.CardName name) {
        if (name.toString().startsWith("Water")) {
            return Card.Element.water;
        } else if (name.toString().startsWith("Fire")) {
            return Card.Element.fire;
        } else {
            return Card.Element.regular;
        }
    }

    private Card.CardType detectCardType(Card.CardName name) {

        if (name == Card.CardName.WaterGoblin || name == Card.CardName.FireGoblin || name == Card.CardName.RegularGoblin ||
                name == Card.CardName.WaterTroll || name == Card.CardName.FireTroll || name == Card.CardName.RegularTroll ||
                name == Card.CardName.WaterElf || name == Card.CardName.FireElf || name == Card.CardName.RegularElf ||
                name == Card.CardName.Knight || name == Card.CardName.Dragon || name == Card.CardName.Ork ||
                name == Card.CardName.Kraken) {
            return Card.CardType.monster;
        }


        if (name == Card.CardName.Wizzard || name == Card.CardName.WaterSpell ||
                name == Card.CardName.FireSpell || name == Card.CardName.RegularSpell) {
            return Card.CardType.spell;
        }


        throw new IllegalArgumentException("Unknown card type for name: " + name);
    }
}
