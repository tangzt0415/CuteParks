package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.testapplication.ControlClass.Filter;
import com.example.testapplication.ControlClass.getCoordinateController;
import com.example.testapplication.EntityClass.Park;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static int postal;
    ArrayList<Park> Parks = null;
    boolean displayParkInfo = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker for current location and move the camera
        LatLng bob = new LatLng(Double.parseDouble(getCoordinateController.resultLat), Double.parseDouble(getCoordinateController.resultLong));
        mMap.setMinZoomPreference(10);
        mMap.addMarker(new MarkerOptions().position(bob).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bob));

        displayParkInfo = getIntent().getExtras().getBoolean("DISPLAY1PARK");
        if (getIntent().getSelector()){
            //display 1 selected park only
            Park park = new Park(Objects.requireNonNull(getIntent().getExtras().getParcelable("PARK")));
            LatLng coordinate = new LatLng(park.getLocationY(), park.getLocationX());
            mMap.addMarker(new MarkerOptions().position(coordinate).title(park.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        } else {
            //display all filtered parks

            // Retrieve all parks
            Filter filter = getIntent().getExtras().getParcelable("FILTER");
            Database db = new Database();
            db.loadAllParksAndUpdateOverallRatings().whenComplete((parks, throwable) -> {
                if (throwable == null) {
                    Parks = filter.filterParks(new ArrayList<Park>(parks));
                    Parks.sort(Comparator.comparingDouble(Park::getDistance));
                    Log.d("no of parks", Integer.toString(Parks.size()));
                    LatLng coordinate[] = new LatLng[Parks.size()];
                    for (int i = 0; i < Parks.size(); i++) {
                        coordinate[i] = new LatLng(Parks.get(i).getLocationY(), Parks.get(i).getLocationX());
                        mMap.addMarker(new MarkerOptions().position(coordinate[i]).title(Parks.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    Log.d("marker", "marker added");
                } else {
                    Log.d("DEBUG_APP", "An error has occured");
                }

            });
        }

    }
}