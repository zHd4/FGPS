package com.fgps.models.geo;

import android.app.Activity;

public class MockingLocationRunnable implements Runnable {
    public static int interval = 1000;

    private final Geo geo;
    private final Activity activity;
    private final Coordinates coordinates;

    public MockingLocationRunnable(Geo geo, Activity activity, Coordinates coordinates) {
        this.geo = geo;
        this.activity = activity;
        this.coordinates = coordinates;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (geo.mockLocation(activity, coordinates)) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ignored) { }
        }

        geo.unmockLocation(activity);
    }
}
