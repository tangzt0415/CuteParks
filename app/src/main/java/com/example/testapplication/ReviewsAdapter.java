package com.example.testapplication;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.EntityClass.Review;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private ArrayList<Review> mReviewList;

    public ReviewsAdapter(ArrayList<Review> reviewsList){
        this.mReviewList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        Review currentReview = mReviewList.get(position);

        String desc = currentReview.getDescription().isEmpty() ? "No Description" : currentReview.getDescription();
        holder.mReviewDescriptionTextView.setText(desc);
        holder.mReviewNameTextView.setText(currentReview.getUserName());
        holder.mReviewRatingBar.setRating(currentReview.getRating().floatValue());
        holder.mReviewRatingValue.setText(currentReview.getRating().toString());
    }

    @Override
    public int getItemCount() {
        return this.mReviewList.size();
    }

    public static class ReviewsViewHolder extends RecyclerView.ViewHolder {

        public TextView mReviewNameTextView;
        public TextView mReviewDescriptionTextView;
        public RatingBar mReviewRatingBar;
        public TextView mReviewRatingValue;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewNameTextView = itemView.findViewById(R.id.reviewItemName);
            mReviewDescriptionTextView = itemView.findViewById(R.id.reviewItemDescription);
            mReviewRatingBar = itemView.findViewById(R.id.reviewItemRatingBar);
            mReviewRatingValue = itemView.findViewById(R.id.reviewItemRatingValue);
        }
    }
}
