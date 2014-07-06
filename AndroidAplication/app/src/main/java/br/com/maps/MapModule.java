package br.com.maps;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import br.com.login.Json;

/**
 * Created by Fernando on 05/07/2014.
 */
public class MapModule implements Json.PostListener{
    public void insertProblem(Location location, GoogleMap mMap, BitmapDescriptor image, String title, String description){
          mMap.addMarker(new MarkerOptions()
                  .position(new LatLng(location.getLatitude(), location.getLongitude()))
                  .title("aaaa"));
        Json json = new Json("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()), null);
        json.post(this);
    }

    @Override
    public void onPostSent(JSONObject jsonObject){
        try {
            JSONObject results = (JSONObject) jsonObject.get("results");
            JSONObject address = (JSONObject) jsonObject.get("address_components");
            String formatedAddress = (String) jsonObject.get("formatted_address");
            Log.e("LoginActivity", formatedAddress);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
