package com.example.testapplication.EntityClass;


import com.google.firebase.firestore.DocumentId;

import java.util.UUID;

public class Favourite {
	@DocumentId
	private String id;
	private String userId;
	private String parkId;
	private String parkName;
	
	//////////////////////////////////////////////////////////
	//Constructors
	public Favourite(){
	
	}
	
	public Favourite(String userId, Park park){
		this.id = UUID.randomUUID().toString();
		this.userId = userId;
		this.parkId = park.getId();
		this.parkName = park.getName();
	}
	
	//////////////////////////////////////////////////////////
	//getters and setters
	public String getId() {
		return userId;
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
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	
}
