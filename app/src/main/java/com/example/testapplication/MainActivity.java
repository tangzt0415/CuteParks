package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.BoundaryClass.getCoordinateUI;

public class MainActivity extends AppCompatActivity {

    int postalAdd;
    EditText postalcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postalcode = (EditText)findViewById(R.id.postalAddress);

        Button submit = (Button)findViewById(R.id.postalBtn);
        Button test = (Button)findViewById(R.id.testButton);
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
                //passing infor to another activity
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
