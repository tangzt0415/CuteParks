package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapplication.BoundaryClass.getCoordinateUI;
import com.example.testapplication.EntityClass.Park;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    int postalAdd;
    EditText postalcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve all parks
        Database db = new Database();
        List<Park> allParks = db.loadAllParks().getNow(new ArrayList<>());


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
