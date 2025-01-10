package org.example.application.monsterGame.entity;

public class Card {

    private String cardName;

    private float cardDamage;

    private boolean monsterType;

    private String elementType;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public float getCardDamage() {
        return cardDamage;
    }

    public void setCardDamage(float cardDamage) {
        this.cardDamage = cardDamage;
    }

    public boolean isMonsterType() {
        return monsterType;
    }

    public void setMonsterType(boolean monsterType) {
        this.monsterType = monsterType;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }
}
