package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.BoundaryClass.getCoordinateUI;

public class MainActivity extends AppCompatActivity {

    int postalAdd;
    EditText postalcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve all parks
        Database db = new Database();
        db.loadAllParks().whenComplete((parks, throwable) -> {
            if (throwable == null){
                // Continue Here
            } else {
                Toast.makeText(MainActivity.this, "Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        postalcode = findViewById(R.id.postalAddress);

        Button submit = findViewById(R.id.postalBtn);
        Button test = findViewById(R.id.testButton);
        Button filtertest = findViewById(R.id.filterTestButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalAdd = Integer.parseInt(postalcode.getText().toString());
                Intent startIntent = new Intent(getApplicationContext(), MapsActivity.class);
                //passing info to another activity
                startIntent.putExtra("com.example.testapplication.SOMETHING",postalAdd);
                startActivity(startIntent);
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalAdd = Integer.parseInt(postalcode.getText().toString());
                Intent startIntent = new Intent(getApplicationContext(), getCoordinateUI.class);
                //passing in for to another activity
                startIntent.putExtra("com.example.testapplication.SOMETHING",postalAdd);
                startActivity(startIntent);
            }
        });

        filtertest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(startIntent);
            }
        });
        

    }
}
