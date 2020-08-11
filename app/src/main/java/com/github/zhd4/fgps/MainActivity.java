package com.github.zhd4.fgps;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.github.zhd4.fgps.controllers.MockLocationController;
import com.github.zhd4.fgps.controllers.ToggleGpsOnClickController;
import com.github.zhd4.fgps.models.geo.Coordinates;
import com.github.zhd4.fgps.models.geo.Geo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requireLocationAccessPermission();

        Button randomCoordinatesButton = findViewById(R.id.randomCoordinatesButton);
        final FloatingActionButton toggleGpsButton = findViewById(R.id.toggleGPS);

        final EditText latitude = findViewById(R.id.latitude);
        final EditText longitude = findViewById(R.id.longitude);

        final Geo geo = new Geo();

        Coordinates currentCoords = geo.getCurrentLocation(this, getApplicationContext());

        if(currentCoords != null) {
            latitude.setText(String.valueOf(currentCoords.getLatitude()));
            longitude.setText(String.valueOf(currentCoords.getLongitude()));
        } else {
            setRandomCoordinates(latitude, longitude, geo);
        }

        setToggleGpsButtonState(toggleGpsButton, getApplicationContext(), geo);

        randomCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRandomCoordinates(latitude, longitude, geo);
                setPointOnMap();
            }
        });

        toggleGpsButton.setOnClickListener(new ToggleGpsOnClickController(
                geo,
                getApplicationContext(),
                this,
                latitude,
                longitude,
                toggleGpsButton
        ));

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getResources().getString(R.string.apiGoogleMapsKey));
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setPointOnMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setPointOnMap() {
        final EditText latitude = findViewById(R.id.latitude);
        final EditText longitude = findViewById(R.id.longitude);

        final Coordinates coordinates = new Coordinates(
                Double.parseDouble(latitude.getText().toString()),
                Double.parseDouble(longitude.getText().toString())
        );

        LatLng latLng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());

        googleMap.clear();
        googleMap.setMinZoomPreference(3);

        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void setToggleGpsButtonState(FloatingActionButton toggleGpsButton, Context context, Geo geo) {
        MockLocationController mockLocationController = new MockLocationController(context,this, geo);

        if(mockLocationController.isMockRunning()) {
            toggleGpsButton.setImageResource(android.R.drawable.ic_media_pause);
            toggleGpsButton.setColorFilter(Color.rgb(255, 64, 64));
        }
    }

    private void setRandomCoordinates(final EditText latitude, final EditText longitude, final Geo geo) {
        latitude.setText(String.valueOf(geo.getRandomLatitude()));
        longitude.setText(String.valueOf(geo.getRandomLongitude()));
    }

    private void requireLocationAccessPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 23);
    }
}
