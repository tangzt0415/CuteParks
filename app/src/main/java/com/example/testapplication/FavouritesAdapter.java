package com.example.testapplication;

import android.annotation.SuppressLint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.EntityClass.Favourite;
import com.example.testapplication.EntityClass.Park;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
    private ArrayList<Park> mParkList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick (int position);
        void onShareItemClick (int position);
        void onRemoveItemClick (int position);
        void onReviewItemClick (int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public FavouritesAdapter(ArrayList<Park> parksList){
        this.mParkList = parksList;
    }

    @NonNull
    @Override
    public FavouritesAdapter.FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new FavouritesAdapter.FavouritesViewHolder(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavouritesAdapter.FavouritesViewHolder holder, int position) {
        Park currentPark = mParkList.get(position);
        String parkName = currentPark.getName();

        holder.mFavouriteParkNameTextView.setText(parkName);
    }

    @Override
    public int getItemCount() {
        return this.mParkList.size();
    }

    public static class FavouritesViewHolder extends RecyclerView.ViewHolder {

        public TextView mFavouriteParkNameTextView;
        public ImageButton mFavouriteParkShareImageButton;
        public ImageButton mFavouriteParkRemoveImageButton;
        public ImageButton mFavouriteParkReviewImageButton;

        public FavouritesViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            mFavouriteParkNameTextView = itemView.findViewById(R.id.favouriteItemParkNameTextView);
            mFavouriteParkShareImageButton = itemView.findViewById(R.id.favouriteItemShareButton);
            mFavouriteParkRemoveImageButton = itemView.findViewById(R.id.favouriteItemRemoveButton);
            mFavouriteParkReviewImageButton = itemView.findViewById(R.id.favouriteItemReviewButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mFavouriteParkShareImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onShareItemClick(position);
                        }
                    }
                }
            });

            mFavouriteParkRemoveImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveItemClick(position);
                        }
                    }
                }
            });

            mFavouriteParkReviewImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onReviewItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
