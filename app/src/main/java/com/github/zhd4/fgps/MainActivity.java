package com.github.zhd4.fgps;

import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
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

        requirePermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        requirePermission(Manifest.permission.ACCESS_FINE_LOCATION);

        FloatingActionButton toggleGpsButton = findViewById(R.id.toggleGPS);

        final EditText latitude = findViewById(R.id.latitude);
        final EditText longitude = findViewById(R.id.longitude);

        final Geo geo = new Geo();

        latitude.setText(String.valueOf(geo.getRandomLatitude()));
        longitude.setText(String.valueOf(geo.getRandomLongitude()));

        toggleGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Coordinates coordinates = new Coordinates(
                        Double.parseDouble(latitude.getText().toString()),
                        Double.parseDouble(longitude.getText().toString())
                );

                if(!geo.setMock(MainActivity.this, coordinates)) {
                    showMessage("Go to Setting -> System -> Developer Options -> Select mock location app");
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("");
        dialogBuilder.setMessage(text);
        dialogBuilder.show();
                /*.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialog.cancel();
                    }
                })
                .show();*/

    }
}
