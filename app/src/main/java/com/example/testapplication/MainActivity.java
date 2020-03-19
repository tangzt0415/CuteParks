package com.example.testapplication;

import android.content.Intent;
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
        db.loadAllParksReviewsAndUpdateUserName().whenComplete((parks, error) -> {
            if (error != null) {
                Log.d("DEBUG_APP", "An error has occurred");
            } else {
                Log.d("DEBUG_APP", "Successfully updated parks reviews usernames");
            }
        });

        postalcode = findViewById(R.id.postalAddress);

        Button SearchButton = findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postalcode.length() != 6) {
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
