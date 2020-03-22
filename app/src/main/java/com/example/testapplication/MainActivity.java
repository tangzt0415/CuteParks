package com.example.testapplication;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.EntityClass.Park;
import com.google.firebase.auth.FirebaseAuth;

import com.example.testapplication.ControlClass.getCoordinateController;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * Main Entry UI of application. Allow users to log in or sign up
 * Takes in user current location(if address is left blank) or searches
 * a postal code address
 */
public class MainActivity extends AppCompatActivity {
    public static TextView result;
    public static int postalAdd;
    public static EditText postalcode;
    double xCoordinate;
    double yCoordinate;
    public static boolean hasCurrentLocation = false;
    public static double currentLat;
    public static double currentLong;


    /**
     * creating UI, also loading current gps location.
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Database db = new Database();
        db.loadAllParksReviewsAndUpdateUserName().whenComplete((parks, error) -> {
            if (error != null) {
                Log.d("DEBUG_APP", "An error has occurred");
            } else {
                Log.d("DEBUG_APP", "Successfully updated parks reviews usernames");
            }
        });
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //TODO: UI updates.
                currentLat = location.getLatitude();
                currentLong = location.getLongitude();
            }
        });
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        postalcode = findViewById(R.id.postalAddress);
        Button SearchButton = findViewById(R.id.SearchButton);
        /**
         * Search button sends location being searched into filterActivity.
         * also checks for validity of postal code entry.
         */
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postalcode.length() == 0){
                    hasCurrentLocation = true;
                    //postalAdd = Integer.parseInt(postalcode.getText().toString());
                    getCoordinateController process = new getCoordinateController();
                    process.execute();

                    Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                    //pass x- and y-coordinates to FilterActivity
                    //intent.putExtra("com.example.testapplication.SOMETHING", postalAdd);
                    startActivity(intent);
                }
                else if (postalcode.length() != 6) {
                    Toast.makeText(getApplicationContext(), "PostalCode is 6 digits only", Toast.LENGTH_SHORT).show();
                }
                else if(postalcode.length() == 6) {
                    hasCurrentLocation = false;

                    postalAdd = Integer.parseInt(postalcode.getText().toString());
                    getCoordinateController process = new getCoordinateController();
                    process.execute();

                    Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                    //pass x- and y-coordinates to FilterActivity
                    intent.putExtra("com.example.testapplication.SOMETHING", postalAdd);
                    startActivity(intent);
                }
            }
        });
        ImageButton favouritesButton = findViewById(R.id.favouritesButton);
        /**
         * Button to go manage favourites, only avaliable after successful log in.
         */
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                db.loadFavouriteParksByUserId(mAuth.getCurrentUser().getUid()).whenComplete((parks, throwable) -> {
                    if (throwable == null) {
                        ArrayList<Park> favouriteArrayList = new ArrayList<>(parks);
                        intent.putParcelableArrayListExtra("PARKS", favouriteArrayList);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Button loginButton = findViewById(R.id.filterLoginButton);
        Button signupButton = findViewById(R.id.filterSignupButton);
        Button logoutButton = findViewById(R.id.filterLogoutButton);
//        .setVisibility(View.GONE)
        if(mAuth.getCurrentUser() == null) {
            logoutButton.setVisibility(View.GONE);
            favouritesButton.setVisibility(View.GONE);
        } else {
            loginButton.setVisibility(View.GONE);
            signupButton.setVisibility(View.GONE);
        }
        /**
         * log out user
         */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "You have successfully signed out.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });

        /**
         * log in user
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        /**
         * sign up user
         */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });




    }
}
