package org.example.application.monsterGame.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String id;

    @JsonProperty("Username")
    private String userName;

    @JsonProperty("Password")
    private String password;

    private String bio;

    private String image;

    private String name;

    private int coin;


    private int elo;

    public User(){

    }

    public User(String username, String password){
        this.userName = username;
        this.password = password;

    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
