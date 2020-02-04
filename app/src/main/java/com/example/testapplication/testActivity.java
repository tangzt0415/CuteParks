package com.example.testapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


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

            final TextView textView = (TextView) findViewById(R.id.text);
// ...

// Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://developers.onemap.sg/commonapi/search?searchVal=640643&returnGeom=Y&getAddrDetails=Y&pageNum=1";

// Request a string response from the provided URL

            JsonObjectRequest Request = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Display the first 500 characters of the response string.
                            //textView.setText("Response is: "+ response.substring(0,500));
                            JSONArray jsonArray = response.getJSONArray("results");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView.setText("That didn't work!");
                }
            });

// Add the request to the RequestQueue.
            queue.add(Request);
        }
    }
}
