package com.example.testapplication.EntityClass;


import com.google.firebase.firestore.DocumentId;

import java.util.UUID;

public class Review {
    @DocumentId
    private String id;
    private String userId;
    private String parkId;
    private Double rating;
    private String description;


    //////////////////////////////////////////////////////////
    //Constructors
    public Review(){

    }

    public Review(String userId, String parkId, Double rating, String description) {
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


    public Double getRating() {
        return rating;
    }


    public void setRating(Double rating) {
        this.rating = rating;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }



}
