package org.example.application.monsterGame.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String id;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("Password")
    private String password;

    private String bio;

    private String image;

    private String name;

    private int coin;

    public User(){

    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
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
        this.username = username;
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
}
