package br.com.maps;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.br.com.utils.Constants;
import br.com.login.Json;

/**
 * Created by Fernando on 05/07/2014.
 */
public class MapModule implements Json.PostListener{
    private String formatedAddress;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();

    public void insertProblem(Location location, GoogleMap mMap, String image, String title, String description){
        // seta parametros de envio

        params.add(new BasicNameValuePair("token", "4e080605bac8a26c6376e00c8ad1131ceb423242"));
        params.add(new BasicNameValuePair("image", image));
        title = "testdsadsadsadsae";
        description = "teste tettewtwtrrfdsfadsfweqrfsdfedsa";
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("description", description));
        params.add(new BasicNameValuePair("latitude", String.valueOf(location.getLatitude())));
        params.add(new BasicNameValuePair("longitude", String.valueOf(location.getLongitude())));





        mMap.addMarker(new MarkerOptions()
                  .position(new LatLng(location.getLatitude(), location.getLongitude()))
                  .title(title));
        Json.post(this, "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "&sensor=true_or_false", null);
    }

    @Override
    public void onPostSent(JSONObject jsonObject){
        try {
            if (jsonObject.get("report") == null){

                JSONArray results = jsonObject.getJSONArray("results");
                JSONObject address = (JSONObject) results.get(0);
                formatedAddress = address.getString("formatted_address");
                Log.e("LoginActivity", formatedAddress);
                params.add(new BasicNameValuePair("address", formatedAddress));
                Json.post(this, Constants.URL + Constants.CREATE_REPORT , params);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
