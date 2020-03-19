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

import com.example.testapplication.ControlClass.getCoordinateController;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    public static TextView result;
    public static int postalAdd;
    public static EditText postalcode;
    double xCoordinate;
    double yCoordinate;
    public static boolean hasCurrentLocation = false;
    public static double currentLat;
    public static double currentLong;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
/*
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            currentLat = location.getLatitude();
                            currentLong = location.getLongitude();
                        }
                    }
                });
*/
        postalcode = findViewById(R.id.postalAddress);
        Button SearchButton = findViewById(R.id.SearchButton);
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
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });

    }
}
