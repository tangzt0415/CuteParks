package com.example.testapplication.EntityClass;


import java.util.UUID;

public class Review {
    private String id;
    private String userId;
    private String parkId;
    private int rating;
    private String description;


    //////////////////////////////////////////////////////////
    //Constructors
    public Review(){

    }

    public Review(String userId, String parkId, int rating, String description) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.parkId = parkId;
        this.rating = rating;
        this.description = description;
    }


    //////////////////////////////////////////////////////////
    //Getters and setters
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getParkId() {
        return parkId;
    }


    public void setParkId(String parkId) {
        this.parkId = parkId;
    }


    public int getRating() {
        return rating;
    }


    public void setRating(int rating) {
        this.rating = rating;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }



}
