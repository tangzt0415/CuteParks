package com.example.testapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class testActivity extends AppCompatActivity {
    public static int postal;
    Button click;
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

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData process = new fetchData();
                process.execute();
            }
        });

    }
    public int getPostal(){
        return postal;
    }
}
