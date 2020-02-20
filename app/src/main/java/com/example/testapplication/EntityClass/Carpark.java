package com.example.testapplication.EntityClass;

public class Carpark {
	private int id;
	private String name;
	private double locationX;
	private double locationY;
	
	
	//////////////////////////////////////////////////////////
	//Constructor
	public Carpark() {
		
	}
	
	public Carpark(int id, String name, double locationX, double locationY) {
		this.id = id;
		this.name = name;
		this.locationX = locationX;
		this.locationY = locationY;
	}
	
	//////////////////////////////////////////////////////////
	//Getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
