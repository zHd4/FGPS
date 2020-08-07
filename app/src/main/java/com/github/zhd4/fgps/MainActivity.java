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
import com.github.zhd4.fgps.geo.Coordinates;
import com.github.zhd4.fgps.geo.Geo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requirePermission(Manifest.permission.ACCESS_FINE_LOCATION);
        requirePermission(Manifest.permission.ACCESS_COARSE_LOCATION);

        Button randomCoordinatesButton = findViewById(R.id.randomCoordinatesButton);
        FloatingActionButton toggleGpsButton = findViewById(R.id.toggleGPS);

        final EditText latitude = findViewById(R.id.latitude);
        final EditText longitude = findViewById(R.id.longitude);

        final Geo geo = new Geo();

        Coordinates currentCoords = geo.getCurrentLocation(this, getApplicationContext());

        latitude.setText(String.valueOf(currentCoords.getLatitude()));
        longitude.setText(String.valueOf(currentCoords.getLongitude()));

        randomCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude.setText(String.valueOf(geo.getRandomLatitude()));
                longitude.setText(String.valueOf(geo.getRandomLongitude()));
            }
        });

        toggleGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Coordinates coordinates = new Coordinates(
                        Double.parseDouble(latitude.getText().toString()),
                        Double.parseDouble(longitude.getText().toString())
                );

                if(!geo.mockLocation(MainActivity.this, coordinates)) {
                    showMessage(getResources().getString(R.string.allowMockMessage));
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

    private void requirePermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{ permission }, 23);
    }

    private void showMessage(String text) {
        new AlertDialog.Builder(MainActivity.this).setTitle("").setMessage(text).show();
    }
}
