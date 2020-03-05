package com.example.testapplication.EntityClass;

import java.util.ArrayList;
import java.math.*;

import android.content.Intent;

public class EntityClassTest {

    public static void main(String[]args){
    	
        User yl=new User("ElinWyl","Yxaw219317!");
        yl.setLocationX(103.8045);
        yl.setLocationY(1.33);

        String UserId = yl.getId();
        double UserLocationX = yl.getLocationX();
        double UserLocationY = yl.getLocationY();
        System.out.println("User "+yl.getName());
        System.out.println("User Location: ("+UserLocationX+", "+UserLocationY + ")\n");
        
        /*User ly = new User("Liying","hahaha");
        ArrayList<User> Users = new ArrayList<User>();
        Users.add(ly);
        Users.add(yl);
        
        System.out.println("Target user ID: "+id);
        */
  
        
        
        ArrayList<Park> Parks = new ArrayList<Park>();
        Parks.add(new Park("Singapore Botanic Gardens","park 1",103.8162506,1.311048144,"1 Cluny Road","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=33&Itemid=73"));
        Parks.add(new Park("Woodlands Waterfront Park","park 2",103.7799362,1.452813755,"Admiralty Road West","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=90&Itemid=73"));
        Parks.add(new Park("Tiong Bahru Park","park 3",103.8243399,1.287589986,"Bounded by Henderson Rd, Tiong Bahru Road and Lower Delta Road","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=37&Itemid=73"));
        Parks.add(new Park("One-North Park","park 4",103.7907746,1.303491325,"The 3.3 hectare site is situated in the northern zone of one-north, near the Ministry of Education","http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=25&Itemid=73"));
        
        Parks.get(0).addReview(new Review(UserId,Parks.get(0).getId(),5,"Perfect!"));
        Parks.get(0).addReview(new Review(UserId,Parks.get(0).getId(),4,"Nice"));
        Parks.get(0).addReview(new Review(UserId,Parks.get(0).getId(),4,"Nice"));
//        System.out.println("Park 1: "+Parks.get(0).getName());
//        System.out.println("Overall Rating: "+Parks.get(0).getOverallRating()+"\n");
        
        Parks.get(1).addReview(new Review(UserId,Parks.get(1).getId(),4,"Perfect!"));
        Parks.get(1).addReview(new Review(UserId,Parks.get(1).getId(),3,"Nice"));
        Parks.get(1).addReview(new Review(UserId,Parks.get(1).getId(),4,"Nice"));
//        System.out.println("Park 2: "+Parks.get(1).getName());
//        System.out.println("Overall Rating: "+Parks.get(1).getOverallRating()+"\n");
        
        Parks.get(2).addReview(new Review(UserId,Parks.get(2).getId(),5,"Perfect!"));
        Parks.get(2).addReview(new Review(UserId,Parks.get(2).getId(),3,"Nice"));
        Parks.get(2).addReview(new Review(UserId,Parks.get(2).getId(),4,"Nice"));
//        System.out.println("Park 3: "+Parks.get(2).getName());
//        System.out.println("Overall Rating: "+Parks.get(2).getOverallRating()+"\n");
        

        Parks.get(3).addReview(new Review(UserId,Parks.get(3).getId(),5,"Perfect!"));
        Parks.get(3).addReview(new Review(UserId,Parks.get(3).getId(),5,"Nice"));
        Parks.get(3).addReview(new Review(UserId,Parks.get(3).getId(),4,"Nice"));
//        System.out.println("Park 4: "+Parks.get(3).getName());
//        System.out.println("Overall Rating: "+Parks.get(3).getOverallRating()+"\n");
        
        
        
        
        //Filters
        String keywordF = "bO";
        int distanceF = 10;
        double ratingF = 3.8;

        
        //Filter by Distance
        int i=0;
        for (Park park:Parks) {
        	i++;
        	park.setDistance(111*Math.sqrt((UserLocationX-park.getLocationX())*(UserLocationX-park.getLocationX())+(UserLocationY-park.getLocationY())*(UserLocationY-park.getLocationY())));
        	if ((park.getDistance() <= distanceF) 
        			& (park.getOverallRating() >= ratingF) 
        			& ( (park.getName().toLowerCase().contains(keywordF.toLowerCase())) 
        					| (park.getLocationAddress().toLowerCase().contains(keywordF.toLowerCase()))
        					| (park.getDescription().toLowerCase().contains(keywordF.toLowerCase()))
        			  ) 
        	) {
        		FilterResults.add(park);
        	}
        }
        
        i=0;
        if (FilterResults.isEmpty()) {
        	System.out.println("No parks fulfill all the requirements... \nWould you like to try something else?\n\n");
        } else {
        	System.out.println("==================================\n          Filter Results\n==================================");
        	for (Park park:FilterResults) {
            	i++;
            	System.out.println("Park "+i+": "+park.getName());
            	System.out.println("Distance: "+park.getDistance()+" km");
            	System.out.println("Overall Rating: "+park.getOverallRating()+"\n");
            }
        }
        
        
        
        
        
        /*
        Parks.get(0).addCarpark(new Carpark(1,"carpark1",1,1));
        Parks.get(0).addCarpark(new Carpark(2,"carpark2",2,2));
        
        
        for (Carpark carpark:Parks.get(0).getNearbyCarparks()) {
        	System.out.println("Carpark "+carpark.getId()+" "+carpark.getName());
        	System.out.println("Carpark Location: ("+carpark.getLocationX()+", "+carpark.getLocationY()+")");
        	
        }
        
        Parks.get(0).addAmenity(new Amenity(1,"amenity1"));
    	System.out.print("Amenity "+Parks.get(0).getAmenities().get(0).getId()+ ": ");
    	System.out.print(Parks.get(0).getAmenities().get(0).getDescription());
        
        
        ArrayList<Favourite> Favourites = new ArrayList<Favourite>();
        Favourites.add(new Favourite(id,Parks.get(0)));
        Favourites.add(new Favourite(id,Parks.get(1)));
        Favourites.add(new Favourite(id,Parks.get(2)));
        Favourites.add(new Favourite(ly.getId(),Parks.get(2)));
        Favourites.add(new Favourite(ly.getId(),Parks.get(3)));
        
        
        
        
        for (User user:Users) {
        	if (user.getId()==id) {
        		
        		
        		System.out.println("User ID: "+r.getUserId());
                System.out.println("Park ID: "+r.getParkId());
                System.out.println("Rating: "+r.getRating());
                System.out.println("Review: "+r.getDescription());
                
                System.out.println("User name: "+user.getName());
                System.out.println("User password: "+user.getPassword());
        	}
        }
        
        
        System.out.println("\n\n");
        
        for (Favourite f:Favourites) {
        	if (f.getUserId()==id) {
        		System.out.println("User ID: "+f.getUserId());
                System.out.println("Park name: "+f.getParkId());
        	}
        }
        
       */
    }
}
