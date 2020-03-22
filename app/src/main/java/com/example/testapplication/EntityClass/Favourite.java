package com.example.testapplication.EntityClass;


import com.google.firebase.firestore.DocumentId;

import java.util.UUID;

/**
 * The type Favourite.
 */
public class Favourite {
	@DocumentId
	private String id;
	private String userId;
	private String parkId;
	private String parkName;

	/**
	 * Instantiates a new Favourite.
	 */
//////////////////////////////////////////////////////////
	//Constructors
	public Favourite(){
	
	}

	/**
	 * Instantiates a new Favourite.
	 *
	 * @param userId the user id
	 * @param park   the park
	 */
	public Favourite(String userId, Park park){
		this.id = UUID.randomUUID().toString();
		this.userId = userId;
		this.parkId = park.getId();
		this.parkName = park.getName();
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
//////////////////////////////////////////////////////////
	//getters and setters
	public String getId() {
		return id;
	}

	/**
	 * Gets user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets user id.
	 *
	 * @param userId the user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets park id.
	 *
	 * @return the park id
	 */
	public String getParkId() {
		return parkId;
	}

	/**
	 * Sets park id.
	 *
	 * @param parkId the park id
	 */
	public void setParkId(String parkId) {
		this.parkId = parkId;
	}

	/**
	 * Gets park name.
	 *
	 * @return the park name
	 */
	public String getParkName() {
		return parkName;
	}

	/**
	 * Sets park name.
	 *
	 * @param parkName the park name
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	
}
