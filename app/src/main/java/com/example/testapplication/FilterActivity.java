package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.ControlClass.Filter;


public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //////////////////////////////////////////////////////////
        //get user location from maps
        final double UserLocationX = 103.8045;
        final double UserLocationY = 1.33;

        //////////////////////////////////////////////////////////
        /////display distance input
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
                String keywordF = filterKeywordEditText.getText().toString();

                //get distance filter
                SeekBar filterDistanceSeekBar = findViewById(R.id.filterDistanceSeekBar);
                TextView distanceTextView = findViewById(R.id.distanceTextView);
                int distanceF = filterDistanceSeekBar.getProgress();
                distanceTextView.setText("Within\n" + distanceF + " km");

                //get rating filter
                RatingBar filterRatingBar = findViewById(R.id.filterRatingBar);
                TextView ratingTextView = findViewById(R.id.ratingTextView);
                double ratingF = filterRatingBar.getRating();
                ratingTextView.setText(ratingF + "");

                //generate filter
                Filter filter = new Filter(keywordF, distanceF, ratingF, UserLocationX, UserLocationY);

                //pass the filtered parks for display
                Intent intent = new Intent(FilterActivity.this, DisplayParksActivity.class);
                intent.putExtra("RESULTS",filter.filterParks());
                startActivity(intent);
            }
        });
    }
}

