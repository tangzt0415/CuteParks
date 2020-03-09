package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.BoundaryClass.getCoordinateUI;
import com.example.testapplication.ControlClass.getCoordinateController;

import java.text.CollationElementIterator;
import java.util.Objects;
import com.example.testapplication.EntityClass.Park;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static TextView result;
    public static int postalAdd;
    EditText postalcode;
    double xCoordinate;
    double yCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView result = findViewById(R.id.result);

        // Retrieve all parks
        Database db = new Database();
        db.loadAllParks().whenComplete((parks, throwable) -> {
            if (throwable != null){
                // Continue Here
                Log.d("DEBUG_APP", throwable.getLocalizedMessage().toString());
            } else {
                Log.d("DEBUG_APP", "Success");
            }
        });


        postalcode = findViewById(R.id.postalAddress);

       Button submit = findViewById(R.id.postalBtn);
        Button SearchButton = findViewById(R.id.SearchButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalAdd = Integer.parseInt(postalcode.getText().toString());
                result.setText("Postcode: "+postalAdd);

                getCoordinateController process = new getCoordinateController();
                process.execute();

                Intent startIntent = new Intent(getApplicationContext(), getCoordinateUI.class);
                //passing info to another activity
                startIntent.putExtra("com.example.testapplication.SOMETHING",postalAdd);
                startActivity(startIntent);
            }
        });
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalAdd = Integer.parseInt(postalcode.getText().toString());

                result.setText("Postcode: "+postalAdd);
                //getCoordinateController getCoordinates = new getCoordinateController(postalAdd);
                xCoordinate = 103.8045;
                yCoordinate = 1.33;
                result.setText(String.format("X: %f, Y: %f", xCoordinate,yCoordinate));

                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                //pass x- and y-coordinates to FilterActivity
                intent.putExtra("XCOORDINATE", xCoordinate);
                intent.putExtra("YCOORDINATE", yCoordinate);
                startActivity(intent);
            }
        });


    }
}
