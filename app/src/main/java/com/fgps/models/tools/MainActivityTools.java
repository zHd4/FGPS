package com.fgps.models.tools;

import android.Manifest;
import android.app.Activity;
import androidx.core.app.ActivityCompat;
import com.fgps.models.geo.Coordinates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivityTools {
    public void setPointOnMap(GoogleMap googleMap, double latitude, double longitude) {
        final Coordinates coordinates = new Coordinates(latitude, longitude);

        LatLng latLng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());

        googleMap.clear();
        googleMap.setMinZoomPreference(4);

        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void requireLocationAccessPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 23);
    }
}
