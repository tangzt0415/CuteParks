package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.testapplication.EntityClass.Park;

public class DisplayParkInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_park_information);

        Park park = getIntent().getExtras().getParcelable("PARK");

        TextView parkName = findViewById(R.id.parkName);
        parkName.setText(park.getName());
    }
}
