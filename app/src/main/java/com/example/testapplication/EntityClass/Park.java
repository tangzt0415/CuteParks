package com.example.testapplication.EntityClass;
import com.google.firebase.firestore.DocumentId;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.UUID;

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
	private ArrayList<Amenity> Amenities;
	private ArrayList<Carpark> NearbyCarparks;
	private ArrayList<Review> Reviews;
	private double distance;
	
	//////////////////////////////////////////////////////////
	//Constructors
	public Park(){
	
	}
	
	public Park(String name, String description, double locationX, double locationY, String locationAddress, String website) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.description = description;
		this.locationX = locationX;
		this.locationY = locationY;
		this.locationAddress = locationAddress;
		this.website = website;
		this.Amenities = new ArrayList<Amenity>();
		this.NearbyCarparks = new ArrayList<Carpark>();
		this.Reviews = new ArrayList<Review>();
	}

    protected Park(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        locationX = in.readDouble();
        locationY = in.readDouble();
        locationAddress = in.readString();
        website = in.readString();
        overallRating = in.readDouble();
        distance = in.readDouble();
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public double getOverallRating() {
		this.setOverallRating();
		return this.overallRating;
	}

	public void setOverallRating() {
		int totalRating = 0;
		int count = 0;
		for (Review review:this.Reviews){
			totalRating += review.getRating();
			count ++;
		}
		if (count != 0) {
			this.overallRating = (double)totalRating/count;
		}
	}
	
	public ArrayList<Amenity> getAmenities() {
		return Amenities;
	}

	public void setAmenities(ArrayList<Amenity> amenities) {
		Amenities = amenities;
	}

	public void addAmenity(Amenity amenity) {
		this.Amenities.add(amenity);
	}

	public ArrayList<Carpark> getNearbyCarparks() {
		return NearbyCarparks;
	}

	public void setNearbyCarparks(ArrayList<Carpark> nearbyCarparks) {
		NearbyCarparks = nearbyCarparks;
	}

	public void addCarpark(Carpark carpark) {
		this.NearbyCarparks.add(carpark);
	}

	public ArrayList<Review> getReviews() {
		return Reviews;
	}
	
	public void setReviews(ArrayList<Review> reviews) {
		Reviews = reviews;
	}
	
	public void addReview(Review review) {
		this.Reviews.add(review);
		this.setOverallRating();
	}
	
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
    }
}
