package org.example.application.monsterGame.entity;

public class Package {


    private long id;
    private Card[] cards;

    private Boolean isAvailable;

    public Package(Card[] cards, Boolean isAvailable) {
        this.cards = cards;
        this.isAvailable = isAvailable;
    }

    public Package(long id, Card[] cards, Boolean isAvailable) {
        this.id = id;
        this.cards = cards;
        this.isAvailable = isAvailable;
    }

    public Package() {
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }
}
