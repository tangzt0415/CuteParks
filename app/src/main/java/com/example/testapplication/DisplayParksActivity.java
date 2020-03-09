package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.testapplication.ControlClass.Filter;
import com.example.testapplication.EntityClass.Park;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisplayParksActivity extends AppCompatActivity {

    public void displayParks(ArrayList<Park> parks){

        //display 1st park
        TextView r1No = findViewById(R.id.r1No);
        r1No.setText("1");
        TextView r1Name = findViewById(R.id.r1Name);
        r1Name.setText(parks.get(0).getName());
        TextView r1Distance = findViewById(R.id.r1Distance);
        r1Distance.setText(String.format("%.2f km",parks.get(0).getDistance()));
        TextView r1Rating = findViewById(R.id.r1Rating);
        r1Rating.setText(String.format("%.1f",parks.get(0).getOverallRating()));


        //display 2nd park, if available
        if (parks.size() > 1) {
            TextView r2No = findViewById(R.id.r2No);
            r2No.setText("2");
            TextView r2Name = findViewById(R.id.r2Name);
            r2Name.setText(parks.get(1).getName());
            TextView r2Distance = findViewById(R.id.r2Distance);
            r2Distance.setText(String.format("%.2f km",parks.get(1).getDistance()));
            TextView r2Rating = findViewById(R.id.r2Rating);
            r2Rating.setText(String.format("%.1f",parks.get(1).getOverallRating()));
        }

        //display 3rd park, if available
        if (parks.size() > 2) {
            TextView r3No = findViewById(R.id.r3No);
            r3No.setText("3");
            TextView r3Name = findViewById(R.id.r3Name);
            r3Name.setText(parks.get(2).getName());
            TextView r3Distance = findViewById(R.id.r3Distance);
            r3Distance.setText(String.format("%.2f km",parks.get(2).getDistance()));
            TextView r3Rating = findViewById(R.id.r3Rating);
            r3Rating.setText(String.format("%.1f",parks.get(2).getOverallRating()));
        }

        //display 4th park, if available
        if (parks.size() > 3) {
            TextView r4No = findViewById(R.id.r4No);
            r4No.setText("4");
            TextView r4Name = findViewById(R.id.r4Name);
            r4Name.setText(parks.get(3).getName());
            TextView r4Distance = findViewById(R.id.r4Distance);
            r4Distance.setText(String.format("%.2f km",parks.get(3).getDistance()));
            TextView r4Rating = findViewById(R.id.r4Rating);
            r4Rating.setText(String.format("%.1f",parks.get(3).getOverallRating()));
        }


        //display 5th park, if available
        if (parks.size() > 4) {
            TextView r5No = findViewById(R.id.r5No);
            r5No.setText("5");
            TextView r5Name = findViewById(R.id.r5Name);
            r5Name.setText(parks.get(4).getName());
            TextView r5Distance = findViewById(R.id.r5Distance);
            r5Distance.setText(String.format("%.2f km",parks.get(4).getDistance()));
            TextView r5Rating = findViewById(R.id.r5Rating);
            r5Rating.setText(String.format("%.1f",parks.get(4).getOverallRating()));
        }

        //display 6th park, if available
        if (parks.size() > 5) {
            TextView r6No = findViewById(R.id.r6No);
            r6No.setText("6");
            TextView r6Name = findViewById(R.id.r6Name);
            r6Name.setText(parks.get(5).getName());
            TextView r6Distance = findViewById(R.id.r6Distance);
            r6Distance.setText(String.format("%.2f km",parks.get(5).getDistance()));
            TextView r6Rating = findViewById(R.id.r6Rating);
            r6Rating.setText(String.format("%.1f",parks.get(5).getOverallRating()));
        }

        //display 7th park, if available
        if (parks.size() > 6) {
            TextView r7No = findViewById(R.id.r7No);
            r7No.setText("7");
            TextView r7Name = findViewById(R.id.r7Name);
            r7Name.setText(parks.get(6).getName());
            TextView r7Distance = findViewById(R.id.r7Distance);
            r7Distance.setText(String.format("%.2f km",parks.get(6).getDistance()));
            TextView r7Rating = findViewById(R.id.r7Rating);
            r7Rating.setText(String.format("%.1f",parks.get(6).getOverallRating()));
        }

        //display 8th park, if available
        if (parks.size() > 7) {
            TextView r8No = findViewById(R.id.r8No);
            r8No.setText("8");
            TextView r8Name = findViewById(R.id.r8Name);
            r8Name.setText(parks.get(7).getName());
            TextView r8Distance = findViewById(R.id.r8Distance);
            r8Distance.setText(String.format("%.2f km",parks.get(7).getDistance()));
            TextView r8Rating = findViewById(R.id.r8Rating);
            r8Rating.setText(String.format("%.1f",parks.get(7).getOverallRating()));
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_parks);

        Filter filter = getIntent().getExtras().getParcelable("FILTER");
        Database db = new Database();
        db.loadAllParks().whenComplete((parks, throwable) -> {
            if (throwable == null){
                ArrayList<Park> Parks = filter.filterParks(new ArrayList<Park>(parks));






                //Back to Filter
                Button backToFilterButton = findViewById(R.id.backToFilterButton);
                backToFilterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DisplayParksActivity.this, FilterActivity.class);
                        startActivity(intent);
                    }
                });


                //display Error message if parks is empty
                TextView noParksTextView = findViewById(R.id.noParksTextView);


                //display parks if parks is not empty
                if (!Parks.isEmpty()){

                    //make Error message disappear
                    noParksTextView.setVisibility(noParksTextView.GONE);

                    //display parks
                    Parks.sort(Comparator.comparingDouble(Park::getDistance));
                    this.displayParks(Parks);

                    //select a park
                    LinearLayout r1 = findViewById(R.id.r1);
                    LinearLayout r2 = findViewById(R.id.r2);
                    LinearLayout r3 = findViewById(R.id.r3);
                    LinearLayout r4 = findViewById(R.id.r4);
                    LinearLayout r5 = findViewById(R.id.r5);
                    LinearLayout r6 = findViewById(R.id.r6);
                    LinearLayout r7 = findViewById(R.id.r7);
                    LinearLayout r8 = findViewById(R.id.r8);
                    TextView r1Name = findViewById(R.id.r1Name);
                    TextView r2Name = findViewById(R.id.r2Name);
                    TextView r3Name = findViewById(R.id.r3Name);
                    TextView r4Name = findViewById(R.id.r4Name);
                    TextView r5Name = findViewById(R.id.r5Name);
                    TextView r6Name = findViewById(R.id.r6Name);
                    TextView r7Name = findViewById(R.id.r7Name);
                    TextView r8Name = findViewById(R.id.r8Name);
                    TextView r1Distance = findViewById(R.id.r1Distance);
                    TextView r2Distance = findViewById(R.id.r2Distance);
                    TextView r3Distance = findViewById(R.id.r3Distance);
                    TextView r4Distance = findViewById(R.id.r4Distance);
                    TextView r5Distance = findViewById(R.id.r5Distance);
                    TextView r6Distance = findViewById(R.id.r6Distance);
                    TextView r7Distance = findViewById(R.id.r7Distance);
                    TextView r8Distance = findViewById(R.id.r8Distance);
                    TextView r1No = findViewById(R.id.r1No);
                    TextView r2No = findViewById(R.id.r2No);
                    TextView r3No = findViewById(R.id.r3No);
                    TextView r4No = findViewById(R.id.r4No);
                    TextView r5No = findViewById(R.id.r5No);
                    TextView r6No = findViewById(R.id.r6No);
                    TextView r7No = findViewById(R.id.r7No);
                    TextView r8No = findViewById(R.id.r8No);
                    TextView r1Rating = findViewById(R.id.r1Rating);
                    TextView r2Rating = findViewById(R.id.r2Rating);
                    TextView r3Rating = findViewById(R.id.r3Rating);
                    TextView r4Rating = findViewById(R.id.r4Rating);
                    TextView r5Rating = findViewById(R.id.r5Rating);
                    TextView r6Rating = findViewById(R.id.r6Rating);
                    TextView r7Rating = findViewById(R.id.r7Rating);
                    TextView r8Rating = findViewById(R.id.r8Rating);

                    Button mapViewButton = findViewById(R.id.mapViewButton);



                    View.OnClickListener clickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DisplayParksActivity.this, DisplayParkInformationActivity.class);
                            switch (v.getId()) {
                                case R.id.r1:
                                case R.id.r1Distance:
                                case R.id.r1Name:
                                case R.id.r1No:
                                case R.id.r1Rating:
                                    intent.putExtra("PARK", Parks.get(0));
                                    break;
                                case R.id.r2:
                                case R.id.r2Distance:
                                case R.id.r2Name:
                                case R.id.r2No:
                                case R.id.r2Rating:
                                    intent.putExtra("PARK", Parks.get(1));
                                    break;
                                case R.id.r3:
                                case R.id.r3Distance:
                                case R.id.r3Name:
                                case R.id.r3No:
                                case R.id.r3Rating:
                                    intent.putExtra("PARK", Parks.get(2));
                                    break;
                                case R.id.r4:
                                case R.id.r4Distance:
                                case R.id.r4Name:
                                case R.id.r4No:
                                case R.id.r4Rating:
                                    intent.putExtra("PARK", Parks.get(3));
                                    break;
                                case R.id.r5:
                                case R.id.r5Distance:
                                case R.id.r5Name:
                                case R.id.r5No:
                                case R.id.r5Rating:
                                    intent.putExtra("PARK", Parks.get(4));
                                    break;
                                case R.id.r6:
                                case R.id.r6Distance:
                                case R.id.r6Name:
                                case R.id.r6No:
                                case R.id.r6Rating:
                                    intent.putExtra("PARK", Parks.get(5));
                                    break;
                                case R.id.r7:
                                case R.id.r7Distance:
                                case R.id.r7Name:
                                case R.id.r7No:
                                case R.id.r7Rating:
                                    intent.putExtra("PARK", Parks.get(6));
                                    break;
                                case R.id.r8:
                                case R.id.r8Distance:
                                case R.id.r8Name:
                                case R.id.r8No:
                                case R.id.r8Rating:
                                    intent.putExtra("PARK", Parks.get(7));
                                    break;
                            }
                            startActivity(intent);
                        }


                    };

                    r1.setOnClickListener(clickListener);
                    r1No.setOnClickListener(clickListener);
                    r1Distance.setOnClickListener(clickListener);
                    r1Name.setOnClickListener(clickListener);
                    r1Rating.setOnClickListener(clickListener);
                    r2.setOnClickListener(clickListener);
                    r2No.setOnClickListener(clickListener);
                    r2Distance.setOnClickListener(clickListener);
                    r2Name.setOnClickListener(clickListener);
                    r2Rating.setOnClickListener(clickListener);
                    r3.setOnClickListener(clickListener);
                    r3No.setOnClickListener(clickListener);
                    r3Distance.setOnClickListener(clickListener);
                    r3Name.setOnClickListener(clickListener);
                    r3Rating.setOnClickListener(clickListener);
                    r4.setOnClickListener(clickListener);
                    r4No.setOnClickListener(clickListener);
                    r4Distance.setOnClickListener(clickListener);
                    r4Name.setOnClickListener(clickListener);
                    r4Rating.setOnClickListener(clickListener);
                    r5.setOnClickListener(clickListener);
                    r5No.setOnClickListener(clickListener);
                    r5Distance.setOnClickListener(clickListener);
                    r5Name.setOnClickListener(clickListener);
                    r5Rating.setOnClickListener(clickListener);
                    r6.setOnClickListener(clickListener);
                    r6No.setOnClickListener(clickListener);
                    r6Distance.setOnClickListener(clickListener);
                    r6Name.setOnClickListener(clickListener);
                    r6Rating.setOnClickListener(clickListener);
                    r7.setOnClickListener(clickListener);
                    r7No.setOnClickListener(clickListener);
                    r7Distance.setOnClickListener(clickListener);
                    r7Name.setOnClickListener(clickListener);
                    r7Rating.setOnClickListener(clickListener);
                    r8.setOnClickListener(clickListener);
                    r8No.setOnClickListener(clickListener);
                    r8Distance.setOnClickListener(clickListener);
                    r8Name.setOnClickListener(clickListener);
                    r8Rating.setOnClickListener(clickListener);

                }



            }
        });

        //////////////////////////////////////////////////////////





    }
}