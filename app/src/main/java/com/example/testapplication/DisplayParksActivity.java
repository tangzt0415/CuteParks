package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
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

                 */

            }
        });

    }
}