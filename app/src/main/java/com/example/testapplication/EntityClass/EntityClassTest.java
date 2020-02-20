package com.example.testapplication.EntityClass;


import java.util.ArrayList;

public class StupidTests {

    public static void main(String[]args){
        User yl=new User("ElinWyl","Yxaw219317!");
        User ly = new User("Liying","hahaha");
        ArrayList<User> Users = new ArrayList<User>();
        Users.add(ly);
        Users.add(yl);


        String id = yl.getId();
        System.out.println("Target user ID: "+id);


        ArrayList<Park> Parks = new ArrayList<Park>();
        Parks.add(new Park("p1","park 1",1,1,"location1","www1"));
        System.out.println("Park "+Parks.get(0).getName());
        Parks.add(new Park("p2","park 2",2,2,"location2","www2"));
        Parks.add(new Park("p3","park 3",3,3,"location3","www3"));
        Parks.add(new Park("p4","park 4",4,4,"location4","www4"));

        Parks.get(0).addReview(new Review(id,Parks.get(0).getId(),5,"Perfect!"));
        System.out.println("Overall Rating: "+Parks.get(0).getOverallRating());
        System.out.println("Review "+Parks.get(0).getReviews());

        Parks.get(0).addReview(new Review(id,Parks.get(0).getId(),4,"Nice"));
        System.out.println("Overall Rating: "+Parks.get(0).getOverallRating());
        System.out.println("Review "+Parks.get(0).getReviews());

        Parks.get(0).addReview(new Review(id,Parks.get(0).getId(),4,"Nice"));
        System.out.println("Overall Rating: "+Parks.get(0).getOverallRating());
        System.out.println("Review "+Parks.get(0).getReviews());

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


        /*

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
