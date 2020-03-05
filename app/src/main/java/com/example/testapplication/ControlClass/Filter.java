package com.example.testapplication.ControlClass;

import com.example.testapplication.EntityClass.Park;

import java.util.ArrayList;

public class Filter {
    String keywordF;
    int distanceF;
    double ratingF;
    double UserLocationX;
    double UserLocationY;

    //Constructors
    public Filter(){

    }

    public Filter(String keywordF, int distanceF, double ratingF, double UserLocationX, double UserLocationY){
        this.distanceF = distanceF;
        this.keywordF = keywordF;
        this.ratingF = ratingF;
        this.UserLocationX = UserLocationX;
        this.UserLocationY = UserLocationY;
    }



    //Filter Parks
    public ArrayList<Park> filterParks(){
        ArrayList<Park> Parks= new ArrayList<Park>();
        ArrayList <Park> FilterResults = new ArrayList<Park>();

        int i=0;
        for (Park park:Parks) {
            i++;
            park.setDistance(111*
                    Math.sqrt((this.UserLocationX-park.getLocationX())
                                *(this.UserLocationX-park.getLocationX())
                            +(this.UserLocationY-park.getLocationY())
                                *(this.UserLocationY-park.getLocationY()))
            );

            if ((park.getDistance() <= this.distanceF)
                    & (park.getOverallRating() >= this.ratingF)
                    & ( (park.getName().toLowerCase().contains(this.keywordF.toLowerCase()))
                        | (park.getLocationAddress().toLowerCase().contains(this.keywordF.toLowerCase()))
                        | (park.getDescription().toLowerCase().contains(this.keywordF.toLowerCase())))
            ) {
                FilterResults.add(park);
            }
        }
        return FilterResults;
    }

    public String getKeywordF() {
        return keywordF;
    }

    public void setKeywordF(String keywordF) {
        this.keywordF = keywordF;
    }

    public int getDistanceF() {
        return distanceF;
    }

    public void setDistanceF(int distanceF) {
        this.distanceF = distanceF;
    }

    public double getRatingF() {
        return ratingF;
    }

    public void setRatingF(double ratingF) {
        this.ratingF = ratingF;
    }

    public double getUserLocationX() {
        return UserLocationX;
    }

    public void setUserLocationX(double userLocationX) {
        UserLocationX = userLocationX;
    }

    public double getUserLocationY() {
        return UserLocationY;
    }

    public void setUserLocationY(double userLocationY) {
        UserLocationY = userLocationY;
    }
}
