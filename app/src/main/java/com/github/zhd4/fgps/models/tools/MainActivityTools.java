package com.github.zhd4.fgps.models.tools;

import android.Manifest;
import android.app.Activity;
import android.widget.EditText;
import androidx.core.app.ActivityCompat;
import com.github.zhd4.fgps.models.geo.Coordinates;
import com.github.zhd4.fgps.models.geo.Geo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivityTools {
    public void setPointOnMap(GoogleMap googleMap, final EditText latitude, final EditText longitude) {

        final Coordinates coordinates = new Coordinates(
                Double.parseDouble(latitude.getText().toString()),
                Double.parseDouble(longitude.getText().toString())
        );

        LatLng latLng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());

        googleMap.clear();
        googleMap.setMinZoomPreference(4);

        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void setRandomCoordinates(final EditText latitude, final EditText longitude, final Geo geo) {
        latitude.setText(String.valueOf(geo.getRandomLatitude()).replace(',', '.'));
        longitude.setText(String.valueOf(geo.getRandomLongitude()).replace(',', '.'));
    }

    public void requireLocationAccessPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 23);
    }
}
