package com.github.zhd4.fgps.geo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;

public class Geo {
    static {
        System.loadLibrary("native-lib");
    }

    public native double getRandomLatitude();
    public native double getRandomLongitude();

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    public boolean setMock(Activity activity, Coordinates coordinates) {
        try {
            Location location = this.createNewLocation(coordinates);
            LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

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
}
