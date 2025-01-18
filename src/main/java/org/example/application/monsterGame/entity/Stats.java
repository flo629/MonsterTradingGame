package org.example.application.monsterGame.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats {

    @JsonProperty("Username")
    private String userName;

    @JsonProperty("wins")
    private int totalWins;

    @JsonProperty("loses")
    private int totalLosses;

    @JsonProperty("elo")
    private int elo;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getTotalWins() {
        return totalWins;
    }

    public void addWins(){
        totalWins += 1;
    }

    public void addLosses(){
        totalLosses += 1;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
