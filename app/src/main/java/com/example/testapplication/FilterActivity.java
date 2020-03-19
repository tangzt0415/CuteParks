package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.testapplication.ControlClass.Filter;
import com.example.testapplication.ControlClass.getCoordinateController;

import java.util.Objects;

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
//                getCoordinateController process = new getCoordinateController();
//                process.execute();
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

