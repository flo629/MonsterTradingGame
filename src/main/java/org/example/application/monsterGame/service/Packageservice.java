package org.example.application.monsterGame.service;

import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.repository.PackageRepository;
import org.postgresql.util.PSQLException;

import java.util.List;
import java.util.UUID;

public class Packageservice {

    private final PackageRepository packageRepository;

    public Packageservice(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }


    public String createPackage(Card[] cards) {
        if(cards.length != 5){
            throw new IllegalArgumentException("Cards must have 5 elements");
        }

        String packageId = UUID.randomUUID().toString();
        packageRepository.savePackage(packageId, cards);

        return packageId;
    }

    public int getUserCoins(String username){
        return packageRepository.getUserCoins(username);
    }

    public String getAvailablePackageId(){
        return packageRepository.findAvailablePackageId();
    }

    public void assignPackageToUser(String packageId, String username){
        List<Card> cards = packageRepository.getCardsFromPackage(packageId);
        packageRepository.updatePackageAvailability(packageId, false);

        for(Card card : cards){
            packageRepository.addOwnership(card.getId(), username);
        }

        packageRepository.deductCoins(username, 5);

    }




}
