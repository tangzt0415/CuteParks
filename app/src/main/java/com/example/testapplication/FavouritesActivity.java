package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testapplication.EntityClass.Park;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    ArrayList<Park> parks = new ArrayList<Park>();

    public void displayFavourites(ArrayList<Park> parks) {

        ///// display 1st park
        // display park name
        TextView f1Name = findViewById(R.id.f1Name);
        f1Name.setText(parks.get(0).getName());

        // copy to clipboard
        // remove from favourites
        // write review

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ////////////////////////////////////////////
        // if favourite list is empty
        TextView emptyFavListTextView = findViewById(R.id.emptyFavListTextView);

        // if not empty
        if (!parks.isEmpty()) {

            // make empty list message disappear
            emptyFavListTextView.setVisibility(emptyFavListTextView.GONE);

            // display favourite parks
            this.displayFavourites(parks);

            // All views
            LinearLayout f1 = findViewById(R.id.f1);
            LinearLayout f2 = findViewById(R.id.f2);
            LinearLayout f3 = findViewById(R.id.f3);
            LinearLayout f4 = findViewById(R.id.f4);
            LinearLayout f5 = findViewById(R.id.f5);
            LinearLayout f6 = findViewById(R.id.f6);
            LinearLayout f7 = findViewById(R.id.f7);
            LinearLayout f8 = findViewById(R.id.f8);
            TextView f1Name = findViewById(R.id.f1Name);
            TextView f2Name = findViewById(R.id.f2Name);
            TextView f3Name = findViewById(R.id.f3Name);
            TextView f4Name = findViewById(R.id.f4Name);
            TextView f5Name = findViewById(R.id.f5Name);
            TextView f6Name = findViewById(R.id.f6Name);
            TextView f7Name = findViewById(R.id.f7Name);
            TextView f8Name = findViewById(R.id.f8Name);
            ImageButton f1ShareButton = findViewById(R.id.f1ShareButton);
            ImageButton f2ShareButton = findViewById(R.id.f2ShareButton);
            ImageButton f3ShareButton = findViewById(R.id.f3ShareButton);
            ImageButton f4ShareButton = findViewById(R.id.f4ShareButton);
            ImageButton f5ShareButton = findViewById(R.id.f5ShareButton);
            ImageButton f6ShareButton = findViewById(R.id.f6ShareButton);
            ImageButton f7ShareButton = findViewById(R.id.f7ShareButton);
            ImageButton f8ShareButton = findViewById(R.id.f8ShareButton);
            ImageButton f1FavouriteButton = findViewById(R.id.f1FavouriteButton);
            ImageButton f2FavouriteButton = findViewById(R.id.f2FavouriteButton);
            ImageButton f3FavouriteButton = findViewById(R.id.f3FavouriteButton);
            ImageButton f4FavouriteButton = findViewById(R.id.f4FavouriteButton);
            ImageButton f5FavouriteButton = findViewById(R.id.f5FavouriteButton);
            ImageButton f6FavouriteButton = findViewById(R.id.f6FavouriteButton);
            ImageButton f7FavouriteButton = findViewById(R.id.f7FavouriteButton);
            ImageButton f8FavouriteButton = findViewById(R.id.f8FavouriteButton);
            ImageButton f1ReviewButton = findViewById(R.id.f1ReviewButton);
            ImageButton f2ReviewButton = findViewById(R.id.f2ReviewButton);
            ImageButton f3ReviewButton = findViewById(R.id.f3ReviewButton);
            ImageButton f4ReviewButton = findViewById(R.id.f4ReviewButton);
            ImageButton f5ReviewButton = findViewById(R.id.f5ReviewButton);
            ImageButton f6ReviewButton = findViewById(R.id.f6ReviewButton);
            ImageButton f7ReviewButton = findViewById(R.id.f7ReviewButton);
            ImageButton f8ReviewButton = findViewById(R.id.f8ReviewButton);



            ///// copy to clipboard


            ///// remove from favourites
            f1FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f1.setVisibility(f1.GONE);
                }
            });
            f2FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f2.setVisibility(f2.GONE);
                }
            });
            f3FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f3.setVisibility(f3.GONE);
                }
            });
            f4FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f4.setVisibility(f4.GONE);
                }
            });
            f5FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f5.setVisibility(f5.GONE);
                }
            });
            f6FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f6.setVisibility(f6.GONE);
                }
            });
            f7FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f7.setVisibility(f7.GONE);
                }
            });
            f8FavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f8.setVisibility(f8.GONE);
                }
            });

            /*View.OnClickListener clickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    switch(v.getId()) {
                        case R.id.f1FavouriteButton:
                            f1.removeView();
                            break;
                        case R.id.f2FavouriteButton:
                            f2.removeView();
                            break;
                        case R.id.f3FavouriteButton:
                            f3.removeView();
                            break;
                        case R.id.f4FavouriteButton:
                            f4.removeView();
                            break;
                        case R.id.f5FavouriteButton:
                            f5.removeView();
                            break;
                        case R.id.f6FavouriteButton:
                            f6.removeView();
                            break;
                        case R.id.f7FavouriteButton:
                            f7.removeView();
                            break;
                        case R.id.f8FavouriteButton:
                            f8.removeView();
                            break;

                    }
                }
            }
            */


            ///// write review




        }


    }
}
