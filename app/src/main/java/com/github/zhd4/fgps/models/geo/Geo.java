package com.github.zhd4.fgps.models.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import androidx.core.app.ActivityCompat;

public class Geo {
    static {
        System.loadLibrary("native-lib");
    }

    private native double randomLatitude();
    private native double randomLongitude();

    public double getRandomLatitude() {
        return roundCoordinate(randomLatitude());
    }

    public double getRandomLongitude() {
        return roundCoordinate(randomLongitude());
    }

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    public boolean mockLocation(Activity activity, Coordinates coordinates) {
        try {
            Location location = this.createNewLocation(coordinates);
            LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

            try {
                manager.addTestProvider(
                        LocationManager.GPS_PROVIDER,
                        false,
                        false,
                        false,
                        false,
                        true,
                        true,
                        true,
                        0,
                        0
                );
            } catch (IllegalArgumentException ignored) { }

            manager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

            manager.setTestProviderStatus(
                    LocationManager.GPS_PROVIDER,
                    LocationProvider.AVAILABLE,
                    null,
                    System.currentTimeMillis()
            );

            manager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Coordinates getCurrentLocation(Activity activity, Context context) {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        try {
            return new Coordinates(roundCoordinate(location.getLatitude()), roundCoordinate(location.getLongitude()));
        } catch (NullPointerException e) {
            return null;
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private Location createNewLocation(Coordinates coordinates) {
        Location location = new Location(LocationManager.GPS_PROVIDER);

        location.setAltitude(0);
        location.setAccuracy(0);

        location.setTime(System.currentTimeMillis());

        location.setLatitude(coordinates.getLatitude());
        location.setLongitude(coordinates.getLongitude());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }

        return location;
    }

    @SuppressLint("DefaultLocale")
    private double roundCoordinate(double coordinate) {
        return Double.parseDouble(String.format("%.6f", coordinate));
    }
}
