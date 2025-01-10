package org.example.application.monsterGame.dto;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserDto {

    @JsonProperty("Name")
    private String name;

    private String userName;

    @JsonProperty("Bio")
    private String bio;

    @JsonProperty("Image")
    private String image;


    public UpdateUserDto() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
