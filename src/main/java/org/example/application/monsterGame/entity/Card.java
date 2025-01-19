package org.example.application.monsterGame.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    public enum CardName {
        WaterGoblin, FireGoblin, RegularGoblin,
        WaterTroll, FireTroll, RegularTroll,
        WaterElf, FireElf, RegularElf,
        WaterSpell, FireSpell, RegularSpell,
        Knight, Dragon, Ork, Kraken, Wizzard
    }

    public enum CardType {
        monster, spell
    }

    public enum Element {
        fire, water, regular
    }

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private CardName name;
    private CardType type;
    private Element element;


    @JsonProperty("Damage")
    private int damage;
    private String ownerId; // Nullable
    private boolean isInDeck;
    private boolean isLocked;


    public Card(String id, CardName name, CardType type, Element element, int damage) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.element = element;
        this.damage = damage;
        this.ownerId = null; // No owner initially
        this.isInDeck = false;
        this.isLocked = false;
    }

    public Card(){

    };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CardName getName() {
        return name;
    }

    public void setName(CardName name) {
        this.name = name;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isInDeck() {
        return isInDeck;
    }

    public void setInDeck(boolean inDeck) {
        isInDeck = inDeck;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", type=" + type +
                ", element=" + element +
                ", damage=" + damage +
                ", ownerId='" + ownerId + '\'' +
                ", isInDeck=" + isInDeck +
                ", isLocked=" + isLocked +
                '}';
    }
}
