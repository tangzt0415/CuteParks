package com.example.testapplication.BoundaryClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.testapplication.ControlClass.Filter;
import com.example.testapplication.ControlClass.getCoordinateController;
import com.example.testapplication.R;

import java.util.Objects;

/**
 * Filtering park UI, allows user to select his/her filters.
 */
public class FilterActivity extends FragmentActivity {
    public static int postal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        if (getIntent().hasExtra("com.example.testapplication.SOMETHING")) {
            //retrieving info
            postal = Objects.requireNonNull(getIntent().getExtras()).getInt("com.example.testapplication.SOMETHING");

        }

        SeekBar filterDistanceSeekBar = findViewById(R.id.filterDistanceSeekBar);
        /**
         * Distance slider on UI to let user select max distance of parks he wants to search.
         * Shows the distance selected as the user slides.
         */
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        distanceTextView.setText("Within\n" + 40 + " km");
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
        /**
         * Rating slider to allow user to select minimum ratings he wants to filter for a park.
         */
        RatingBar filterRatingBar = findViewById(R.id.filterRatingBar);

        TextView ratingTextView = findViewById(R.id.ratingTextView);
        ratingTextView.setText("0.0");
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
        /**
         * filter button, on click sends the filtered information to filter entity.
         * also checks for validity of postal code entered on OneMap API.
         */
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCoordinateController.Found == 0){
                    Toast.makeText(getApplicationContext(), "Invalid Postal Code", Toast.LENGTH_SHORT).show();
                }
                else {
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
                    ratingTextView.setText(String.format("%.1f", ratingF));

                    //generate filter
                    Filter filter = new Filter(keywordF, distanceF, ratingF, Double.parseDouble(getCoordinateController.resultLong), Double.parseDouble(getCoordinateController.resultLat));

                    //pass the filtered parks for display
                    Intent intent = new Intent(FilterActivity.this, DisplayParksActivity.class);
                    intent.putExtra("FILTER", filter);
                    startActivity(intent);
                }
            }
        });
    }
}

