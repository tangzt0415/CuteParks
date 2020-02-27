package com.example.testapplication.BoundaryClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.ControlClass.getCoordinateController;
import com.example.testapplication.MapsActivity;
import com.example.testapplication.R;

import java.util.Objects;

//test class to send postal code to Map API to retrieve coordinates
public class getCoordinateUI extends AppCompatActivity {
    public static int postal;
    Button click;
    Button click2;
    public static TextView data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        if (getIntent().hasExtra("com.example.testapplication.SOMETHING")) {
            TextView tv = (TextView) findViewById(R.id.textView2);
            //retrieving info
            postal = Objects.requireNonNull(getIntent().getExtras()).getInt("com.example.testapplication.SOMETHING");
            tv.setText(String.valueOf(postal));

        }
        click = (Button)findViewById(R.id.getCoordinate);
        data = (TextView)findViewById(R.id.displayJson);
        click2 = (Button)findViewById(R.id.setMap);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCoordinateController process = new getCoordinateController();
                process.execute();
            }
        });
        click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MapsActivity.class);
                //passing info to another activity
                //startIntent.putExtra("com.example.testapplication.SOMETHING",postalAdd);
                startActivity(startIntent);
            }
        });
    }
    public int getPostal(){
        return postal;
    }
}
