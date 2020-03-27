package com.example.testapplication.ControlClass;

import android.os.AsyncTask;
import android.util.Log;

//import com.example.testapplication.BoundaryClass.getCoordinateUI;
import com.example.testapplication.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The type Get coordinate controller.
 * background boundary class to fetch LongLat coordinates from oneMap API given a postalCode and convert
 * it into (x,y) coordinates.
 */

public class getCoordinateController extends AsyncTask<Void,Void,Void> {
    private String data = "";
    JSONObject locationObj;
    JSONArray jArray;
    public static String resultLat;
    public static String resultLong;
    public static int Found;
    String address;

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            //setup URL
            URL url = new URL("https://developers.onemap.sg/commonapi/search?searchVal="+ MainActivity.postalAdd+"&returnGeom=Y&getAddrDetails=Y&pageNum=1");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream =  httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //read json into data
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            //calculates Lat/Long
            locationObj = new JSONObject(data);
            Found = locationObj.getInt("found");
            jArray = locationObj.getJSONArray("results");
            locationObj = jArray.getJSONObject(0);
            if(MainActivity.hasCurrentLocation == false) {
                resultLat = locationObj.getString("LATITUDE");
                resultLong = locationObj.getString("LONGITUDE");
            }else {
                resultLat = Double.toString(MainActivity.currentLat);
                resultLong = Double.toString(MainActivity.currentLong);
                if(resultLat.equals("0.0")||resultLong.equals("0.0")){
                    resultLat = "1.290270";
                    resultLong = "103.851959";
                }
                Log.d("lat =", resultLat);
                Log.d("long =", resultLong);
            }
            address = locationObj.getString("ADDRESS");



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}