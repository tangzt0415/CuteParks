package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.testapplication.ControlClass.Filter;
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

        Filter filter = getIntent().getExtras().getParcelable("FILTER");
        ArrayList<Park> parks = filter.filterParks();
        //////////////////////////////////////////////////////////
        //
        //Button listViewButton = findViewById(R.id.listViewButton);
        //Button mapViewButton = findViewById(R.id.mapViewButton);


        //Back to Filter
        Button backToFilterButton = findViewById(R.id.backToFilterButton);
        backToFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayParksActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });


        //display Error message if parks is empty
        TextView noParksTextView = findViewById(R.id.noParksTextView);

        //display parks if parks is not empty
        if (!parks.isEmpty()){
            //make Error message disappear
            noParksTextView.setVisibility(noParksTextView.GONE);

            //display 1st park
            TextView r1No = findViewById(R.id.r1No);
            r1No.setText("1");
            TextView r1Name = findViewById(R.id.r1Name);
            r1Name.setText(parks.get(0).getName());
            TextView r1Distance = findViewById(R.id.r1Distance);
            r1Distance.setText(String.format("%.2f km",parks.get(0).getDistance()));
            TextView r1Rating = findViewById(R.id.r1Rating);
            r1Rating.setText(String.format("%.1f",parks.get(0).getOverallRating()));


            //display 2nd park, if available
            if (parks.size() > 1) {
                TextView r2No = findViewById(R.id.r2No);
                r2No.setText("2");
                TextView r2Name = findViewById(R.id.r2Name);
                r2Name.setText(parks.get(1).getName());
                TextView r2Distance = findViewById(R.id.r2Distance);
                r2Distance.setText(String.format("%.2f km",parks.get(1).getDistance()));
                TextView r2Rating = findViewById(R.id.r2Rating);
                r2Rating.setText(String.format("%.1f",parks.get(1).getOverallRating()));
            }

            //display 3rd park, if available
            if (parks.size() > 2) {
                TextView r3No = findViewById(R.id.r3No);
                r3No.setText("3");
                TextView r3Name = findViewById(R.id.r3Name);
                r3Name.setText(parks.get(2).getName());
                TextView r3Distance = findViewById(R.id.r3Distance);
                r3Distance.setText(String.format("%.2f km",parks.get(2).getDistance()));
                TextView r3Rating = findViewById(R.id.r3Rating);
                r3Rating.setText(String.format("%.1f",parks.get(2).getOverallRating()));
            }

            //display 4th park, if available
            if (parks.size() > 3) {
                TextView r4No = findViewById(R.id.r4No);
                r4No.setText("4");
                TextView r4Name = findViewById(R.id.r4Name);
                r4Name.setText(parks.get(3).getName());
                TextView r4Distance = findViewById(R.id.r4Distance);
                r4Distance.setText(String.format("%.2f km",parks.get(3).getDistance()));
                TextView r4Rating = findViewById(R.id.r4Rating);
                r4Rating.setText(String.format("%.1f",parks.get(3).getOverallRating()));
            }


            //display 5th park, if available
            if (parks.size() > 4) {
                TextView r5No = findViewById(R.id.r5No);
                r5No.setText("5");
                TextView r5Name = findViewById(R.id.r5Name);
                r5Name.setText(parks.get(4).getName());
                TextView r5Distance = findViewById(R.id.r5Distance);
                r5Distance.setText(String.format("%.2f km",parks.get(4).getDistance()));
                TextView r5Rating = findViewById(R.id.r5Rating);
                r5Rating.setText(String.format("%.1f",parks.get(4).getOverallRating()));
            }

            //display 6th park, if available
            if (parks.size() > 5) {
                TextView r6No = findViewById(R.id.r6No);
                r6No.setText("6");
                TextView r6Name = findViewById(R.id.r6Name);
                r6Name.setText(parks.get(5).getName());
                TextView r6Distance = findViewById(R.id.r6Distance);
                r6Distance.setText(String.format("%.2f km",parks.get(5).getDistance()));
                TextView r6Rating = findViewById(R.id.r6Rating);
                r6Rating.setText(String.format("%.1f",parks.get(5).getOverallRating()));
            }

            //display 7th park, if available
            if (parks.size() > 6) {
                TextView r7No = findViewById(R.id.r7No);
                r7No.setText("7");
                TextView r7Name = findViewById(R.id.r7Name);
                r7Name.setText(parks.get(6).getName());
                TextView r7Distance = findViewById(R.id.r7Distance);
                r7Distance.setText(String.format("%.2f km",parks.get(6).getDistance()));
                TextView r7Rating = findViewById(R.id.r7Rating);
                r7Rating.setText(String.format("%.1f",parks.get(6).getOverallRating()));
            }

            //display 8th park, if available
            if (parks.size() > 7) {
                TextView r8No = findViewById(R.id.r8No);
                r8No.setText("8");
                TextView r8Name = findViewById(R.id.r8Name);
                r8Name.setText(parks.get(7).getName());
                TextView r8Distance = findViewById(R.id.r8Distance);
                r8Distance.setText(String.format("%.2f km",parks.get(7).getDistance()));
                TextView r8Rating = findViewById(R.id.r8Rating);
                r8Rating.setText(String.format("%.1f",parks.get(7).getOverallRating()));
            }


        }
        /*

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