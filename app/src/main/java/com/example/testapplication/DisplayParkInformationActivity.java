package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.EntityClass.Review;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.Objects;

public class DisplayParkInformationActivity<ParkName> extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_park_information);
        Database db = new Database();

        Park park = Objects.requireNonNull(getIntent().getExtras()).getParcelable("PARK");

        // Display park name
        TextView parkName = findViewById(R.id.parkName);
        parkName.setText(park.getName());

        // Display park description
        TextView parkDescription = findViewById(R.id.parkDescription);
        parkDescription.setText(park.getDescription());
        parkDescription.setMovementMethod(new ScrollingMovementMethod());

        // Display park rating
        TextView parkRating = findViewById(R.id.parkRating);
        parkRating.setText(String.format("%.1f", park.getOverallRating()));
        RatingBar parkRatingBar = findViewById(R.id.parkRatingBar);
        parkRatingBar.setRating((float) park.getOverallRating());

        //Read Reviews
        Button readReviewsButton = findViewById(R.id.readReviewsButton);
        readReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayParkInformationActivity.this, ReadReviewActivity.class);
                db.loadAllReviewsAndUpdateUserName(park.getId()).whenComplete((reviews, error) -> {
                    ArrayList<Review> reviewsArrayList = new ArrayList<>(reviews);
                    intent.putParcelableArrayListExtra("REVIEWS", reviewsArrayList);
                    startActivity(intent);
                });
            }
        });


        // Display park address
        TextView parkAddress = findViewById(R.id.parkAddress);
        parkAddress.setText(park.getLocationAddress());

        // Display park website
        TextView parkWebsite = findViewById(R.id.parkWebsite);
        parkWebsite.setText(park.getWebsite());

        // Display park location on google map
        MapView googleMap = findViewById(R.id.googleMap);
        googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayParkInformationActivity.this, MapsActivity.class);
                intent.putExtra("PARKmap", park);
                startActivity(intent);
            }
        });

        // Display park activities - GOT PROBLEM - Amenities list of all parks are empty(?)
        TextView parkActivities = findViewById(R.id.parkActivities);

        db.loadPark(park.getId()).whenComplete((park1, throwable) -> {
                    if (throwable == null) {
                        ArrayList<String> activities = new ArrayList<String>(park1.getAmenities());


                        String parkActivitiesString = "";
                        if ((activities.size() == 0)) {
                            parkActivitiesString = "No activities recorded.";
                        } else {
                            int i = 0;
                            for (String activity : activities) {
                                i++;
                                parkActivitiesString = parkActivitiesString + i + ". "
                                        + activity.substring(0,1).toUpperCase() + activity.substring(1) + "\n";
                            }
                        }
                        String textToDisplay = parkActivitiesString;
                        parkActivities.setText(parkActivitiesString);
                        parkActivities.setMovementMethod(new ScrollingMovementMethod());
                    }
        });


     // Share park
        ImageButton sharePark = findViewById(R.id.sharePark);

        // Favourite park
        ImageButton favouritePark = findViewById(R.id.favouritePark);

        // Review park
        ImageButton reviewPark = findViewById(R.id.reviewPark);
        reviewPark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayParkInformationActivity.this, AddReviewActivity.class);
                intent.putExtra("parkId", park.getId());
                startActivity(intent);
            }
        });
    }
}
