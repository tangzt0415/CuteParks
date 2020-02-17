package com.example.testapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//boundary class to fetch LongLat coordinates from oneMap API given a postalCode
//Is a background class incorporating oneMap API
public class fetchData extends AsyncTask<Void,Void,Void> {
    private String data = "";
    @Override
    protected Void doInBackground(Void... voids) {

        try {
            //setup URL
            URL url = new URL("https://developers.onemap.sg/commonapi/search?searchVal="+testActivity.postal+"&returnGeom=Y&getAddrDetails=Y&pageNum=1");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream =  httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //read json into data
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        testActivity.data.setText(this.data);
    }

}
