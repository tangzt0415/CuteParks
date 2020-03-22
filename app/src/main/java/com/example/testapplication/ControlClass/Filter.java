package com.example.testapplication.ControlClass;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


//import com.example.testapplication.Database;
import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.EntityClass.Review;
import com.example.testapplication.EntityClass.User;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The type Filter.
 * This class creates an Filter Object that calculates distance,rating and search keywords
 * and sets an ArrayList of Parks which satisfies the filter options.
 * Implements Parcelable to parse  from one activity to another.
 */
public class Filter implements Parcelable {
    String keywordF;
    int distanceF;
    double ratingF;
    double UserLocationX;
    double UserLocationY;

    //Constructors
    public Filter(){
    }


    /**
     * Instantiates a new Filter.
     *
     * @param keywordF      the keyword in search
     * @param distanceF     the distance selected in filter
     * @param ratingF       the rating seleected in filter
     * @param UserLocationX the user location x (latitude)
     * @param UserLocationY the user location y (Longitude)
     */
    public Filter(String keywordF, int distanceF, double ratingF, double UserLocationX, double UserLocationY){
        this.distanceF = distanceF;
        this.keywordF = keywordF;
        this.ratingF = ratingF;
        this.UserLocationX = UserLocationX;
        this.UserLocationY = UserLocationY;
    }

    protected Filter(Parcel in) {
        keywordF = in.readString();
        distanceF = in.readInt();
        ratingF = in.readDouble();
        UserLocationX = in.readDouble();
        UserLocationY = in.readDouble();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    /**
     * Filter parks  into an array list.
     * distance is calculated based on x,y coordinate using 	1° ≈ 111 km
     * @param Parks the parks
     * @return the array list
     */
    public ArrayList<Park> filterParks(ArrayList<Park> Parks){

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
                        |(park.getDescription().toLowerCase().contains(this.keywordF.toLowerCase())))
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(keywordF);
        dest.writeInt(distanceF);
        dest.writeDouble(ratingF);
        dest.writeDouble(UserLocationX);
        dest.writeDouble(UserLocationY);
    }
}
