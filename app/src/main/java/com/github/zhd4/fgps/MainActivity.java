package com.github.zhd4.fgps;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.github.zhd4.fgps.controllers.EditTextOnChangedController;
import com.github.zhd4.fgps.controllers.ToggleGpsOnClickController;
import com.github.zhd4.fgps.models.geo.Coordinates;
import com.github.zhd4.fgps.models.geo.Geo;
import com.github.zhd4.fgps.models.tools.MainActivityTools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final MainActivityTools tools = new MainActivityTools();

    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tools.requireLocationAccessPermission(MainActivity.this);

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
            tools.setRandomCoordinates(latitude, longitude, geo);
        }

        tools.setToggleGpsButtonState(toggleGpsButton, getApplicationContext(), MainActivity.this, geo);

        randomCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools.setRandomCoordinates(latitude, longitude, geo);
                tools.setPointOnMap(googleMap, latitude, longitude);
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
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        final Geo geo = new Geo();
        final EditText latitude = findViewById(R.id.latitude);
        final EditText longitude = findViewById(R.id.longitude);

        tools.setPointOnMap(googleMap, latitude, longitude);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);
                googleMap.clear();

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);

                latitude.setText(String.valueOf(geo.roundCoordinate(latLng.latitude)));
                longitude.setText(String.valueOf(geo.roundCoordinate(latLng.longitude)));
            }
        });

        latitude.addTextChangedListener(new EditTextOnChangedController(latitude, longitude, googleMap));
        longitude.addTextChangedListener(new EditTextOnChangedController(latitude, longitude, googleMap));
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
}
