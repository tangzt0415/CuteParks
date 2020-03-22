package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.EntityClass.Favourite;
import com.google.firebase.auth.FirebaseAuth;
import com.example.testapplication.EntityClass.Park;
import com.example.testapplication.EntityClass.Review;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * UI to display information of a single park and its attributes mainly
 * name, description, rating, reviews, amenities, and website.
 * Allows link to map display via map click
 * Allows add review via review button
 * Allows sharing of park info by share button
 * Allows adding park to favourites by favourite button
 * @param <ParkName> the type parameter
 */
public class DisplayParkInformationActivity<ParkName> extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_park_information);

        Database db = new Database();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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

//                if (mAuth.getCurrentUser() != null) {
//                    Intent intent = new Intent(DisplayParkInformationActivity.this, ReadReviewActivity.class);
//                    db.loadAllReviewsAndUpdateUserName(park.getId()).whenComplete((reviews, error) -> {
//                        ArrayList<Review> reviewsArrayList = new ArrayList<>(reviews);
//                        intent.putParcelableArrayListExtra("REVIEWS", reviewsArrayList);
//                        startActivity(intent);
//                    });
//                } else {
//                    Toast.makeText(DisplayParkInformationActivity.this, "Please sign in first to use this feature!", Toast.LENGTH_SHORT).show();
//                }
            }
        });


        // Display park address
        TextView parkAddress = findViewById(R.id.parkAddress);
        parkAddress.setText(park.getLocationAddress());

        // Display park website
        TextView parkWebsite = findViewById(R.id.parkWebsite);
        parkWebsite.setText(park.getWebsite());

        // Display park location on google map
        /**
         * Change display into a Map View, displaying current user and the park as markers
         */
        MapView googleMap = findViewById(R.id.googleMap);
        googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayParkInformationActivity.this, MapsActivity.class);
                intent.putExtra("PARKmap", park);
                startActivity(intent);
            }
        });

        // Display park activities
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
        sharePark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("PARK_DETAILS", park.printParkInformation());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);

                Toast.makeText(DisplayParkInformationActivity.this, "Park details has been copied to clip board!", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Favourite button to add park into user's favourites, only available once logged in.
         */
        ImageButton favouritePark = findViewById(R.id.favouritePark);
        if (mAuth.getCurrentUser() == null) {
            favouritePark.setVisibility(View.GONE);
        } else {
            favouritePark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        Favourite fav = new Favourite(uid, park);
                        db.createFavourite(fav).whenComplete((favouriteId, error) -> {
                            if (error == null) {
                                if (!favouriteId.equals("")) {
                                    Toast.makeText(DisplayParkInformationActivity.this, "You have successfully favourited this park!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DisplayParkInformationActivity.this, "You have already favourited this park!", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(DisplayParkInformationActivity.this, "An error has occurred and this park is not favourited!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else{
                        Toast.makeText(DisplayParkInformationActivity.this, "Please sign in first to use this feature!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        /**
         * button to add review for a park
         */
        ImageButton reviewPark = findViewById(R.id.reviewPark);
        reviewPark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(DisplayParkInformationActivity.this, AddReviewActivity.class);
                    intent.putExtra("PARK_ID", park.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(DisplayParkInformationActivity.this, "Please sign in first to use this feature!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
