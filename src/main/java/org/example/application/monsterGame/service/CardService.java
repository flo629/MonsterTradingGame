package org.example.application.monsterGame.service;

import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.repository.CardRespository;

public class CardService {

    private final CardRespository cardRespository;

    public CardService(CardRespository cardRespository) {
        this.cardRespository = cardRespository;
    }

    public void addCard(Card card) {
        cardRespository.save(card);
    }


}
