package org.example.application.monsterGame.service;

import org.example.application.monsterGame.entity.Card;
import org.example.application.monsterGame.entity.Deck;
import org.example.application.monsterGame.repository.CardRespository;
import org.example.application.monsterGame.repository.DeckRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeckService {

    private final DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public Optional<Deck> getDeckByUsername(String username) {
        return deckRepository.getDeckByUsername(username);
    }

    public void configureDeck(String username, String[] cardIds) {
        List<Card> cards = new ArrayList<>();
        System.out.println("arraylist erstellt");
        for (String cardId : cardIds) {
            Card card = deckRepository.getCardByIdAndUsername(cardId, username)
                    .orElseThrow(() -> new IllegalArgumentException("Card with ID " + cardId + " does not belong to the user or does not exist"));
            cards.add(card);
        }
        System.out.println("Cards list erstellt");
        Deck deck = new Deck(cards);
        deckRepository.saveDeck(username, deck);
    }
}
