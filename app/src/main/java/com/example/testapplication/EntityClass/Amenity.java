package com.example.testapplication.EntityClass;


public class Amenity {
    private int id;
    private String description;


    //////////////////////////////////////////////////////////
    //Constructor
    public Amenity() {

    }

    public Amenity(int id, String description) {
        this.id = id;
        this.description = description;
    }

    //////////////////////////////////////////////////////////
    //Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
