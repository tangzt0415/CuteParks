package com.example.testapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //////////////////////////////////////////////////////////
        //display distance input
        SeekBar filterDistanceSeekBar = findViewById(R.id.filterDistanceSeekBar);

        filterDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView distanceTextView = findViewById(R.id.distanceTextView);
                distanceTextView.setText("Within\n" + progress + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        //////////////////////////////////////////////////////////
        //display rating input
        RatingBar filterRatingBar = findViewById(R.id.filterRatingBar);
        filterRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                TextView ratingTextView = findViewById(R.id.ratingTextView);
                ratingTextView.setText(rating + "");
            }
        });



        //////////////////////////////////////////////////////////
        //finalise keyword, distance and rating filters input
        Button filterButton = findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                //start filtering

            }
        });
    }
}

