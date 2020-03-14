package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.BoundaryClass.getCoordinateUI;
import com.example.testapplication.ControlClass.getCoordinateController;
import com.example.testapplication.EntityClass.Park;

public class MainActivity extends AppCompatActivity {
    public static TextView result;
    public static int postalAdd;
    public static EditText postalcode;
    double xCoordinate;
    double yCoordinate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database db = new Database();
        db.loadAllParksAndUpdateOverallRatings().whenComplete(((parks, throwable) -> {
            if (throwable == null) {
                // Your code here
            } else {
                Log.d("DEBUG_APP", "An error has occured");
            }
        }));

        postalcode = findViewById(R.id.postalAddress);

        Button SearchButton = findViewById(R.id.SearchButton);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalAdd = Integer.parseInt(postalcode.getText().toString());

                getCoordinateController process = new getCoordinateController();
                process.execute();

                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                //pass x- and y-coordinates to FilterActivity
                intent.putExtra("com.example.testapplication.SOMETHING",postalAdd);
                startActivity(intent);
            }
        });

        ImageButton favouritesButton = findViewById(R.id.favouritesButton);
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
