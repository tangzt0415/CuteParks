package com.example.testapplication.ControlClass;

import android.os.AsyncTask;

import com.example.testapplication.BoundaryClass.getCoordinateUI;

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
//boundary class to fetch LongLat coordinates from oneMap API given a postalCode
//Is a background class incorporating oneMap API
public class getCoordinateController extends AsyncTask<Void,Void,Void> {
    private String data = "";
    JSONObject locationObj;
    JSONArray jArray;
    public static String resultLat;
    public static String resultLong;
    String results;

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            //setup URL
            URL url = new URL("https://developers.onemap.sg/commonapi/search?searchVal="+ getCoordinateUI.postal+"&returnGeom=Y&getAddrDetails=Y&pageNum=1");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream =  httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //read json into data
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            locationObj = new JSONObject(data);
            jArray = locationObj.getJSONArray("results");
            locationObj = jArray.getJSONObject(0);
            resultLat = locationObj.getString("LATITUDE");
            resultLong = locationObj.getString("LONGITUDE");
            results = "Lat = " + resultLat + " Long =" + resultLong;


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
        getCoordinateUI.data.setText(results);

    }

}