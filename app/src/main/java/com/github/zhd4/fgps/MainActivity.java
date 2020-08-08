package com.github.zhd4.fgps;

import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.github.zhd4.fgps.controllers.MockLocationController;
import com.github.zhd4.fgps.controllers.MockLocationResult;
import com.github.zhd4.fgps.models.geo.Coordinates;
import com.github.zhd4.fgps.models.geo.Geo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
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

        randomCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRandomCoordinates(latitude, longitude, geo);
            }
        });

        toggleGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Coordinates coordinates = new Coordinates(
                        Double.parseDouble(latitude.getText().toString()),
                        Double.parseDouble(longitude.getText().toString())
                );

                /*if(!geo.mockLocation(MainActivity.this, coordinates)) {
                    showMessage(getResources().getString(R.string.allowMockMessage));
                }*/

                MockLocationController mockController = new MockLocationController(
                        getApplicationContext(),MainActivity.this, geo
                );

                if(!mockController.isMockRunning()) {
                    MockLocationResult startMockResult = mockController.startMock(coordinates);

                    if(startMockResult.equals(MockLocationResult.SUCCESS)) {
                        toggleGpsButton.setImageResource(android.R.drawable.ic_media_pause);
                    } else if(startMockResult.equals(MockLocationResult.FAIL)) {
                        showMessage(getResources().getString(R.string.allowMockMessage));
                    }
                } else {
                    MockLocationResult stopMockResult = mockController.stopMock();

                    if(stopMockResult.equals(MockLocationResult.SUCCESS) ||
                            stopMockResult.equals(MockLocationResult.IGNORE)) {
                        toggleGpsButton.setImageResource(android.R.drawable.ic_media_play);
                    } else if(stopMockResult.equals(MockLocationResult.FAIL)) {
                        showMessage(getResources().getString(R.string.allowMockMessage));
                    }
                }
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void showMessage(String text) {
        new AlertDialog.Builder(MainActivity.this).setTitle("").setMessage(text).show();
    }
}
