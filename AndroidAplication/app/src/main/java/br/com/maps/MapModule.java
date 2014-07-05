package br.com.maps;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Fernando on 05/07/2014.
 */
public class MapModule {
    public static void insertProblem(Location location, GoogleMap mMap, BitmapDescriptor image){
          mMap.addMarker(new MarkerOptions()
                  .position(new LatLng(location.getLatitude(), location.getLongitude()))
                  .title("Hello world").icon(image));
    }
}
