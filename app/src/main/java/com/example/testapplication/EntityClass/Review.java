package com.example.testapplication.EntityClass;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

import java.util.UUID;

/**
 * This class implements review entity with the attributes:
 * review id, name and id of user who posted the review, id of the park linked to the review
 * rating the user gave, and his/her comments.
 */
public class Review implements Parcelable {
    @DocumentId
    private String id;
    private String userName;
    private String userId;
    private String parkId;
    private Double rating;
    private String description;


    //////////////////////////////////////////////////////////
    //Constructors
    public Review(){

    }

    public Review(String id, String userName, String userId, String parkId, Double rating, String description) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.parkId = parkId;
        this.rating = rating;
        this.description = description;
    }


    protected Review(Parcel in) {
        id = in.readString();
        userName = in.readString();
        userId = in.readString();
        parkId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(userId);
        dest.writeString(parkId);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    //////////////////////////////////////////////////////////
    //Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
