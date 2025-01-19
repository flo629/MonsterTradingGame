package org.example.application.monsterGame.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck(List<Card> cards) {
        if (cards.size() != 4) {
            throw new IllegalArgumentException("A deck must contain exactly 4 cards");
        }
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }
}
