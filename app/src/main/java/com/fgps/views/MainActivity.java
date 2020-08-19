package com.fgps.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.fgps.R;
import com.fgps.controllers.onclick.ToggleGpsController;
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

    public Coordinates coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mapView = findViewById(R.id.mapView);

        tools.requireLocationAccessPermission(MainActivity.this);

        FloatingActionButton randomCoordinatesButton = findViewById(R.id.randomCoordinatesButton);
        final FloatingActionButton toggleGpsButton = findViewById(R.id.toggleGPS);

        final Geo geo = new Geo();

        Coordinates currentCoords = geo.getCurrentLocation(this, getApplicationContext());

        if (currentCoords != null) {
            this.coordinates = currentCoords;
        } else {
            this.coordinates = geo.getRandomCoordinates();
        }

        randomCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.coordinates = geo.getRandomCoordinates();
                tools.setPointOnMap(googleMap, MainActivity.this.coordinates);
            }
        });

        toggleGpsButton.setOnClickListener(new ToggleGpsController(
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

        tools.setPointOnMap(googleMap, MainActivity.this.coordinates);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);
                googleMap.clear();

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);

                MainActivity.this.coordinates = new Coordinates(latLng.latitude, latLng.longitude);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.actionAbout:
                GUITools.showMessage(MainActivity.this, getResources().getString(R.string.aboutText));
                break;
            case R.id.actionSettings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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
