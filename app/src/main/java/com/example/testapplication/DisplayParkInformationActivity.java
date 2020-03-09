package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.testapplication.EntityClass.Park;
import com.google.android.gms.maps.MapView;

public class DisplayParkInformationActivity<ParkName> extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_park_information);

        Park park = getIntent().getExtras().getParcelable("PARK");

        /*TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("AbhiAndroid"); //set text for text view */

        // Display park name
        TextView parkName = findViewById(R.id.parkName);
        parkName.setText(park.getName());

        // Display park rating
        TextView parkRating = findViewById(R.id.parkRating);
        parkRating.setText((int) park.getOverallRating());

        // Display park description
        TextView parkDescription = findViewById(R.id.parkDescription);
        parkDescription.setText(park.getDescription());

        // Display park address
        TextView parkAddress = findViewById(R.id.parkAddress);
        parkAddress.setText(park.getLocationAddress());

        // Display park website
        TextView parkWebsite = findViewById(R.id.parkWebsite);
        parkWebsite.setText(park.getWebsite());

        // Display park location on google map
        MapView googleMap = findViewById(R.id.googleMap);

        // Share park
        ImageButton sharePark = findViewById(R.id.sharePark);

        // Favourite park
        ImageButton favouritePark = findViewById(R.id.favouritePark);

        // Review park
        ImageButton reviewPark = findViewById(R.id.reviewPark);
    }
}

/*    //get distance filter
        SeekBar filterDistanceSeekBar = findViewById(R.id.filterDistanceSeekBar);
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        int distanceF = filterDistanceSeekBar.getProgress();
        distanceTextView.setText("Within\n" + distanceF + " km");

                        //get rating filter
                        RatingBar filterRatingBar = findViewById(R.id.filterRatingBar);
                        TextView ratingTextView = findViewById(R.id.ratingTextView);
                        double ratingF = filterRatingBar.getRating();
                        ratingTextView.setText(String.format("%.1f",ratingF));

 */