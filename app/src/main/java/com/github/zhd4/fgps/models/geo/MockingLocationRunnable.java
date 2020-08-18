package com.github.zhd4.fgps.models.geo;

import android.app.Activity;

public class MockingLocationRunnable implements Runnable {
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
                Thread.sleep(700);
            } catch (InterruptedException ignored) { }
        }

        geo.unmockLocation(activity);
    }
}
