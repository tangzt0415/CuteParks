package com.example.testapplication.EntityClass;
import com.google.firebase.firestore.DocumentId;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The type Park. Park entity that stores information of a particular park
 * with the attributes park id , name of park , small description of park,
 * x and y location, location in street/word, URL webpage, overall rating of park,
 * amenities it has, distance to user location.
 * Parcelable to allow park object to be passed from activities.
 */
public class Park implements Parcelable{
	@DocumentId
	private String id;
	private String name;
	private String description;
	private double locationX;
	private double locationY;
	private String locationAddress;
	private String website;
	private double overallRating;
	private ArrayList Amenities;
//	private ArrayList<Carpark> NearbyCarparks;
//	private ArrayList<Review> Reviews;
	private double distance;
	//Constructors
	public Park(){
	
	}
	
	public Park(String id, String name, String description, double locationX, double locationY, String locationAddress, String website, ArrayList<String> amenities) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.locationX = locationX;
		this.locationY = locationY;
		this.locationAddress = locationAddress;
		this.website = website;
		this.Amenities = amenities;
//		this.NearbyCarparks = new ArrayList<>();
//		this.Reviews = new ArrayList<>();
	}

    public Park(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        locationX = in.readDouble();
        locationY = in.readDouble();
        locationAddress = in.readString();
        website = in.readString();
        overallRating = in.readDouble();
        distance = in.readDouble();
        Amenities = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<Park> CREATOR = new Creator<Park>() {
        @Override
        public Park createFromParcel(Parcel in) {
            return new Park(in);
        }

        @Override
        public Park[] newArray(int size) {
            return new Park[size];
        }
    };
	//Getters and setters
	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public void setId() { this.id = UUID.randomUUID().toString(); }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public double getLocationX() { return locationX; }

	public void setLocationX(double locationX) { this.locationX = locationX; }

	public double getLocationY() { return locationY; }

	public void setLocationY(double locationY) { this.locationY = locationY; }

	public String getLocationAddress() { return locationAddress; }

	public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }

	public String getWebsite() { return website; }

	public void setWebsite(String website) { this.website = website; }

	public double getOverallRating() { return overallRating;}

    public void setOverallRating(double overallRating) { this.overallRating = overallRating; }

    @SuppressLint("DefaultLocale")
	public String printParkInformation() {
		String amenities = String.join(",", this.getAmenities());
		return String.format("Park Name: %s\n Park Location: %f %f \n Website: %s\n Overall Rating: %f\n Amenities: %s", name, locationX, locationY, website, overallRating, amenities);
	}

	public ArrayList<String> getAmenities() { return Amenities; }

	public void setAmenities(ArrayList<String> amenities) { Amenities = amenities; }

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(locationX);
        dest.writeDouble(locationY);
        dest.writeString(locationAddress);
        dest.writeString(website);
        dest.writeDouble(overallRating);
        dest.writeDouble(distance);
        dest.writeList(Amenities);
    }
}