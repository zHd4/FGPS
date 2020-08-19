package com.fgps.models.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import androidx.core.app.ActivityCompat;

public class Geo {
    public static int accuracy = 1;

    static {
        System.loadLibrary("native-lib");
    }

    public Coordinates getRandomCoordinates() {
        return new Coordinates(roundCoordinate(randomLatitude()), roundCoordinate(randomLongitude()));
    }

    @SuppressLint("DefaultLocale")
    public double roundCoordinate(double coordinate) {
        return Double.parseDouble(String.format("%.9f", coordinate).replace(',', '.'));
    }

    public boolean mockLocation(Activity activity, Coordinates coordinates) {
        try {
            LocationManager manager = createManager(activity);
            String provider = getBestProvider(manager);
            Location location = this.createNewLocation(coordinates, provider);

            manager = prepareManager(manager, provider, accuracy);

            assert manager != null;

            manager.setTestProviderEnabled(provider, true);
            manager.setTestProviderLocation(provider, location);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void unmockLocation(Activity activity) {
        try {
            unmockLocation(activity, LocationManager.GPS_PROVIDER);
            unmockLocation(activity, LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void unmockLocation(Activity activity, String provider) {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        manager.removeTestProvider(provider);
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

    public boolean testAccuracy(Activity activity, int accuracy) {
        LocationManager manager = createManager(activity);
        String provider = getBestProvider(manager);

        return prepareManager(manager, provider, accuracy) != null;
    }

    private LocationManager createManager(Activity activity) {
        return (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    private String getBestProvider(LocationManager manager) {
        return manager.getBestProvider(new Criteria(), false);
    }

    private LocationManager prepareManager(final LocationManager manager, String provider, int accuracy) {
        try {
            manager.addTestProvider(
                    provider,
                    false,
                    false,
                    false,
                    false,
                    false,
                    true,
                    true,
                    1,
                    accuracy);

            return manager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private Location createNewLocation(Coordinates coordinates, String provider) {
        Location location = new Location(provider);

        location.setBearing(1F);
        location.setAccuracy(3F);

        location.setSpeed(0.01F);
        location.setAltitude(location.getAltitude());
        location.setTime(System.currentTimeMillis());

        location.setLatitude(coordinates.getLatitude());
        location.setLongitude(coordinates.getLongitude());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            location.setBearingAccuracyDegrees(0.1F);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            location.setVerticalAccuracyMeters(0.1F);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            location.setSpeedAccuracyMetersPerSecond(0.01F);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }

        return location;
    }

    private native double randomLatitude();
    private native double randomLongitude();
}
