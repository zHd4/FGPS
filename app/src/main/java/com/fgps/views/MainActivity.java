package com.fgps.views;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.fgps.R;
import com.fgps.controllers.ToggleGpsOnClickController;
import com.fgps.models.geo.Coordinates;
import com.fgps.models.geo.Geo;
import com.fgps.models.tools.GUITools;
import com.fgps.models.tools.MainActivityTools;
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

    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);

        tools.requireLocationAccessPermission(MainActivity.this);

        FloatingActionButton randomCoordinatesButton = findViewById(R.id.randomCoordinatesButton);
        final FloatingActionButton toggleGpsButton = findViewById(R.id.toggleGPS);

        final Geo geo = new Geo();

        Coordinates currentCoords = geo.getCurrentLocation(this, getApplicationContext());

        if (currentCoords != null) {
            latitude = currentCoords.getLatitude();
            longitude = currentCoords.getLongitude();
        } else {
            latitude = geo.getRandomLatitude();
            longitude = geo.getRandomLongitude();
        }

        randomCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = geo.getRandomLatitude();
                longitude = geo.getRandomLongitude();

                tools.setPointOnMap(googleMap, latitude, longitude);
            }
        });

        toggleGpsButton.setOnClickListener(new ToggleGpsOnClickController(
                geo, getApplicationContext(),
                this, toggleGpsButton
        ));

        Bundle mapViewBundle = null;

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(
                    savedInstanceState.getString("com.google.android.geo.API_KEY")
            );
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        final Geo geo = new Geo();

        tools.setPointOnMap(googleMap, latitude, longitude);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);
                googleMap.clear();

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);

                latitude = geo.roundCoordinate(latLng.latitude);
                longitude = geo.roundCoordinate(latLng.longitude);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionAbout) {
            GUITools.showMessage(MainActivity.this, getResources().getString(R.string.aboutText));
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
