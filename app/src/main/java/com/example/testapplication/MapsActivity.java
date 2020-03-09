package com.example.testapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static int postal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Retrieve all parks
        Database db = new Database();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(getIntent().hasExtra("com.example.testapplication.SOMETHING")){
            int postal = getIntent().getExtras().getInt("com.example.testapplication.SOMETHING");
        }
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
        Park park[] = new Park[3];
        LatLng coordinate[] = new LatLng[3];
        park[0] = new Park(UUID.randomUUID().toString()
                ,"Jurong Central Park", "Fitness corner or stations and fitness equipment or exercise station   Grand lawn   Jogging path or running track   Playground   Restroom/Toilets with or without shower facilities   Event lawn"
                , 103.7075606
                , 1.337442349
                , "Jalan Boon Lay junction with Boon Lay Way"
                , "http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=68&Itemid=73"
                , new ArrayList<>());
        park[1] = new Park(UUID.randomUUID().toString()
                ,"Kranji Resevoir Park", "Drinking fountain   Toilets   Shelters   Benches"
                , 103.7380629
                ,1.439122404
                ,"Along Kranji Way"
                ,"http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=86&Itemid=73"
                , new ArrayList<>());
        park[2] = new Park(UUID.randomUUID().toString()
                ,"Choa Chu Kang Park"
                ,"Amphitheatre   Benches   Bicycle rack   Childrens play equipment   Food and beverage area or restaurant or cafe   Fitness corner or stations and fitness equipment or exercise station   Jogging path or running track   Multi-purpose court or corner   Restroom or Toilets with or without shower facilities"
                ,103.7471915,1.387737263,"Beside Kranji Expressway and along Choa Chu Kang Drive"
                ,"http://www.nparks.gov.sg/cms/index.php?option=com_visitorsguide&task=parks&id=12&Itemid=73"
                , new ArrayList<>());

        // Add a marker for current location and move the camera
        LatLng bob = new LatLng(Double.parseDouble(getCoordinateController.resultLat), Double.parseDouble(getCoordinateController.resultLong));
        mMap.setMinZoomPreference(10);
        mMap.addMarker(new MarkerOptions().position(bob).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bob));
        for(int i = 0;i < 3; i++) {
            coordinate[i] = new LatLng(park[i].getLocationY(), park[i].getLocationX());
            mMap.addMarker(new MarkerOptions().position(coordinate[i]).title(park[i].getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
    }
}