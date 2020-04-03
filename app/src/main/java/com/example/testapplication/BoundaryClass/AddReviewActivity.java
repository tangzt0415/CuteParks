package com.example.testapplication.BoundaryClass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.EntityClass.Review;
import com.example.testapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

/**
 * The type Add review activity UI that allows user to add a review to a
 * park, with ratings out of 5 and a small description of park.
 * Reviews are stored into database directly
 */
public class AddReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        Intent intent = getIntent();
        String parkId = intent.getStringExtra("PARK_ID");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Database db = new Database();

//        Button backButton = findViewById(R.id.addReviewBackButton);
        Button saveButton = findViewById(R.id.addReviewSaveButton);
        RatingBar ratingBar = findViewById(R.id.addReviewRatingBar);
        TextView reviewTextView = findViewById(R.id.addReviewReviewTextView);

        TextView ratingTrace= findViewById(R.id.ratingTrace);
        ratingTrace.setText("0.0");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                TextView ratingTrace = findViewById(R.id.ratingTrace);
                ratingTrace.setText(rating + "");
            }
        });

        /**
         * Save rating and description of current park into database
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentUserName = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getDisplayName() : "Default Name";
                String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "4cEYV6N7S4MOwknvi0Mw";
                @SuppressLint("DefaultLocale") Double rating = Double.parseDouble(String.format("%f", ratingBar.getRating()));
                String reviewDescription = reviewTextView.getText().toString();

                if (!reviewDescription.isEmpty()) {
                    Review newReview = new Review(UUID.randomUUID().toString(), currentUserName, currentUserId, parkId, rating, reviewDescription);
                    db.createReview(newReview).whenComplete((reviewId, error) -> {
                        if (error != null) {
                            Toast.makeText(AddReviewActivity.this, "An error has occurred and the review is not saved.", Toast.LENGTH_SHORT).show();
                        } else {
                            db.loadAllParksAndUpdateOverallRatings().whenComplete((p, err) -> {
                                Toast.makeText(AddReviewActivity.this, "The review has been saved successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            });

                        }
                    });
                } else {
                    Toast.makeText(AddReviewActivity.this, "Please fill in the review description.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


    }
}
