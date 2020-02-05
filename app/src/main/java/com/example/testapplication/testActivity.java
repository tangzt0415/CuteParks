package com.example.testapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        if (getIntent().hasExtra("com.example.testapplication.SOMETHING")) {
            TextView tv = (TextView) findViewById(R.id.textView2);
            //retrieving info
            int postal = getIntent().getExtras().getInt("com.example.testapplication.SOMETHING");
            tv.setText(String.valueOf(postal));
            
        }
    }
}
