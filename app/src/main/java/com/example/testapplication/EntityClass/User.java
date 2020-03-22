package com.example.testapplication.EntityClass;

import com.google.firebase.firestore.DocumentId;

import java.util.UUID;

/**
 * The class User implements the user entity which stores information about
 * user's id, name, email and password. LocationX and Y are the current coordinate of USER, or
 * the location he searches.
 */
public class User {
    @DocumentId
    private String id;
    private String name;
    private String email;
    private String password;
    private double locationX;
    private double locationY;

    //////////////////////////////////////////////////////////
    //Constructors
    public User() {

    }

    public User(String id, String name, String password, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.locationX = 0;
        this.locationY = 0;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return name; }

    public void setEmail(String email) { this.email = email; }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }
}
