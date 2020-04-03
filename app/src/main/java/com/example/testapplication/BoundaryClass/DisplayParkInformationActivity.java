package com.example.testapplication.BoundaryClass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.EntityClass.Favourite;
import com.example.testapplication.R;
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

    Database db = new Database();

    public void displayParkName(Park park){
        TextView parkName = findViewById(R.id.parkName);
        parkName.setText(park.getName());
    }

    public void displayParkDescription(Park park) {
        TextView parkDescription = findViewById(R.id.parkDescription);
        parkDescription.setText(park.getDescription());
        parkDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    public void displayParkRating(Park park) {
        TextView parkRating = findViewById(R.id.parkRating);
        double rating = park.getOverallRating();
        parkRating.setText(String.format("%.1f", rating));
        RatingBar parkRatingBar = findViewById(R.id.parkRatingBar);
        parkRatingBar.setRating((float) park.getOverallRating());
    }

    public void displayParkReviews(Park park) {
        Intent intent = new Intent(DisplayParkInformationActivity.this, ReadReviewActivity.class);
        Database db = new Database();
        db.loadAllReviewsAndUpdateUserName(park.getId()).whenComplete((reviews, error) -> {
            ArrayList<Review> reviewsArrayList = new ArrayList<>(reviews);
            intent.putParcelableArrayListExtra("REVIEWS", reviewsArrayList);
            startActivity(intent);
        });
    }

    public void displayParkAddress(Park park) {
        TextView parkAddress = findViewById(R.id.parkAddress);
        parkAddress.setText(park.getLocationAddress());
    }

    public void displayParkWebsite(Park park) {
        TextView parkWebsite = findViewById(R.id.parkWebsite);
        parkWebsite.setText(park.getWebsite());
    }

    public void displayParkLocation(Park park) {
        Intent intent = new Intent(DisplayParkInformationActivity.this, MapsActivity.class);
        intent.putExtra("PARKmap", park);
        startActivity(intent);
    }

    public void displayParkActivities(Park park) {
        TextView parkActivities = findViewById(R.id.parkActivities);
        Database db = new Database();
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
                                + activity.substring(0, 1).toUpperCase() + activity.substring(1) + "\n";
                    }
                }
                String textToDisplay = parkActivitiesString;
                parkActivities.setText(parkActivitiesString);
                parkActivities.setMovementMethod(new ScrollingMovementMethod());
            }
        });
    }

    public void shareParkInfo(Park park) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("PARK_DETAILS", park.printParkInformation());
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);

        Toast.makeText(DisplayParkInformationActivity.this, "Park details has been copied to clip board!", Toast.LENGTH_SHORT).show();
    }

    public void favouritePark(Park park){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ImageButton favouritePark = findViewById(R.id.favouritePark);
        if (mAuth.getCurrentUser() == null) {
            favouritePark.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            String uid = mAuth.getCurrentUser().getUid();
            Database db = new Database();
            db.loadFavouriteByParkAndUserId(park.getId(), uid).whenComplete((fav, err) -> {
                if (err != null) {
                    Toast.makeText(DisplayParkInformationActivity.this, "An error has occurred when loading your favourites!", Toast.LENGTH_SHORT).show();
                } else {
                    if (fav != null) {
                        favouritePark.setImageResource(R.drawable.ic_favorite_filled_24dp);
                    } else {
                        favouritePark.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                }
            });
        }

        favouritePark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    String uid = mAuth.getCurrentUser().getUid();
                    Favourite fav = new Favourite(uid, park);
                    Database db = new Database();
                    db.createFavouriteElseDelete(fav).whenComplete((favouriteId, error) -> {
                        if (error == null) {
                            // "" means that the object is deleted
                            if (!favouriteId.equals("")) {
                                Toast.makeText(DisplayParkInformationActivity.this, "You have successfully favourited this park!", Toast.LENGTH_SHORT).show();
                                favouritePark.setImageResource(R.drawable.ic_favorite_filled_24dp);
                            } else {
                                Toast.makeText(DisplayParkInformationActivity.this, "You have successfully unfavourited this park!", Toast.LENGTH_SHORT).show();
                                favouritePark.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            }

                        } else {
                            Toast.makeText(DisplayParkInformationActivity.this, "An error has occurred and this park is not favourited!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    Toast.makeText(DisplayParkInformationActivity.this, "Please sign in first to use this feature!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DisplayParkInformationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void addParkReview(Park park) {
        ImageButton reviewPark = findViewById(R.id.reviewPark);
        reviewPark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(DisplayParkInformationActivity.this, AddReviewActivity.class);
                    intent.putExtra("PARK_ID", park.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(DisplayParkInformationActivity.this, "Please sign in first to use this feature!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DisplayParkInformationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void setUp() {

        Park p = Objects.requireNonNull(getIntent().getExtras()).getParcelable("PARK");
        String pid = p.getId();
        db.loadPark(pid).whenComplete((park, error) -> {
            // Display park name
            displayParkName(park);

            // Display park description
            displayParkDescription(park);

            // Display park rating
            displayParkRating(park);

            //Read Reviews
            Button readReviewsButton = findViewById(R.id.readReviewsButton);
            readReviewsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayParkReviews(park);
                }
            });

            // Display park address
            displayParkAddress(park);

            // Display park website
            displayParkWebsite(park);

            // Display park location on google map
            /**
             * Change display into a Map View, displaying current user and the park as markers
             */
            MapView googleMap = findViewById(R.id.googleMap);
            googleMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayParkLocation(park);
                }
            });

            // Display park activities
            displayParkActivities(park);

            // Share park
            ImageButton sharePark = findViewById(R.id.sharePark);
            sharePark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareParkInfo(park);
                }
            });

            //Favourite button to add park into user's favourites, only available once logged in.
            favouritePark(park);

            //button to add review for a park
            addParkReview(park);

        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_display_park_information);
        super.onCreate(savedInstanceState);
        setUp();
    }

    @Override
    protected void onResume() {
        setContentView(R.layout.activity_display_park_information);
        super.onResume();
        setUp();
    }
}
