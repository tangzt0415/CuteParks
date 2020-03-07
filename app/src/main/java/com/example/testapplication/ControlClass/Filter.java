package com.example.testapplication.ControlClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.EntityClass.Review;
import com.example.testapplication.EntityClass.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class Filter implements Parcelable {
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

    //Generate Parks
    public ArrayList<Park> generateParks() {
        ArrayList<Park> Parks= new ArrayList<Park>();
        User yl=new User("ElinWyl","Yxaw219317!");

        String UserId = yl.getId();
        Parks.add(new Park(UUID.randomUUID().toString(), "Singapore Botanic Gardens","park 1",103.8162506,1.311048144,"1 Cluny Road","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=33&Itemid=73", new ArrayList<>()));
        Parks.add(new Park(UUID.randomUUID().toString(), "Woodlands Waterfront Park","park 2",103.7799362,1.452813755,"Admiralty Road West","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=90&Itemid=73", new ArrayList<>()));
        Parks.add(new Park(UUID.randomUUID().toString(), "Tiong Bahru Park","park 3",103.8243399,1.287589986,"Bounded by Henderson Rd, Tiong Bahru Road and Lower Delta Road","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=37&Itemid=73", new ArrayList<>()));
        Parks.add(new Park(UUID.randomUUID().toString(), "One-North Park","park 4",103.7907746,1.303491325,"The 3.3 hectare site is situated in the northern zone of one-north, near the Ministry of Education","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=25&Itemid=73", new ArrayList<>()));

        Parks.get(0).addReview(new Review(UserId,Parks.get(0).getId(),5,"Perfect!"));
        Parks.get(0).addReview(new Review(UserId,Parks.get(0).getId(),4,"Nice"));
        Parks.get(0).addReview(new Review(UserId,Parks.get(0).getId(),4,"Nice"));

        Parks.get(1).addReview(new Review(UserId,Parks.get(1).getId(),4,"Perfect!"));
        Parks.get(1).addReview(new Review(UserId,Parks.get(1).getId(),3,"Nice"));
        Parks.get(1).addReview(new Review(UserId,Parks.get(1).getId(),4,"Nice"));

        Parks.get(2).addReview(new Review(UserId,Parks.get(2).getId(),5,"Perfect!"));
        Parks.get(2).addReview(new Review(UserId,Parks.get(2).getId(),3,"Nice"));
        Parks.get(2).addReview(new Review(UserId,Parks.get(2).getId(),4,"Nice"));


        Parks.get(3).addReview(new Review(UserId,Parks.get(3).getId(),5,"Perfect!"));
        Parks.get(3).addReview(new Review(UserId,Parks.get(3).getId(),5,"Nice"));
        Parks.get(3).addReview(new Review(UserId,Parks.get(3).getId(),4,"Nice"));
        return Parks;
    }


    //Filter Parks
    public ArrayList<Park> filterParks(){
        ArrayList<Park> Parks = this.generateParks();
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
