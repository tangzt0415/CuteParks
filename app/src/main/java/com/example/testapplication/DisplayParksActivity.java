package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.testapplication.EntityClass.Park;

import java.util.ArrayList;

public class DisplayParksActivity extends AppCompatActivity {

    String keywordF;
    int distanceF;
    double ratingF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_parks);

        ArrayList<Park> parks = new ArrayList<Park>();
        parks = getIntent().getExtras().getParcelableArrayList("RESULTS");

        //////////////////////////////////////////////////////////
        //finalise keyword, distance and rating filters input
        Button listViewButton = findViewById(R.id.listViewButton);
        Button mapViewButton = findViewById(R.id.mapViewButton);

        TextView r1No= findViewById(R.id.r1No);
        r1No.setText("1");
        TextView r1Name= findViewById(R.id.r1Name);
        r1Name.setText(parks.get(0).getName());
        TextView r1Distance= findViewById(R.id.r1Distance);
        r1Distance.setText(parks.get(0).getDistance()+"km");
        TextView r1Rating= findViewById(R.id.r1Rating);
        r1Rating.setText(parks.get(0).getOverallRating()+"");

        /*
        final ArrayList<Park> finalParks = parks;

        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LinearLayout ListView = findViewById(R.id.ListView);

                TextView r1No= findViewById(R.id.r1No);
                r1No.setText("1");
                TextView r1Name= findViewById(R.id.r1Name);
                r1Name.setText(finalParks.get(0).getName());
                TextView r1Distance= findViewById(R.id.r1Distance);
                r1Distance.setText(finalParks.get(0).getDistance()+"km");
                TextView r1Rating= findViewById(R.id.r1Rating);
                r1Rating.setText(finalParks.get(0).getOverallRating()+"");

         */
/*
                for (int i=0; i<finalParks.size(); i++){

                }

                //get keyword filter
                EditText filterKeywordEditText = findViewById(R.id.filterKeywordEditText);
                String keyword = filterKeywordEditText.getText().toString();

                //get distance filter
                SeekBar filterDistanceSeekBar = findViewById(R.id.filterDistanceSeekBar);
                TextView distanceTextView = findViewById(R.id.distanceTextView);
                int distance = filterDistanceSeekBar.getProgress();
                distanceTextView.setText("Within\n" + distance + " km");

                //get rating filter
                RatingBar filterRatingBar = findViewById(R.id.filterRatingBar);
                TextView ratingTextView = findViewById(R.id.ratingTextView);
                ratingTextView.setText(filterRatingBar.getRating() + "");



            }
        });
*/
    }
}