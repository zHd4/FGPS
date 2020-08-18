package com.fgps.controllers;

import android.app.Activity;
import com.fgps.models.geo.Coordinates;
import com.fgps.models.geo.Geo;
import com.fgps.models.geo.MockingLocationRunnable;

public class MockLocationController {
    private final Geo geo;
    private final Activity activity;

    private Thread mockingThread;

    public MockLocationController(Activity activity, Geo geo) {
        this.activity = activity;
        this.geo = geo;
    }

    public MockLocationResult startMock(Coordinates newCoordinates) {
        MockLocationResult result = MockLocationResult.SUCCESS;

        if(geo.mockLocation(activity, newCoordinates)) {
            this.mockingThread = new Thread(new MockingLocationRunnable(geo, activity, newCoordinates));
            this.mockingThread.start();
        } else {
            result = MockLocationResult.FAIL;
        }

        return result;
    }

    @SuppressWarnings({"deprecation", "unused"})
    public MockLocationResult stopMock() {
        MockLocationResult result = MockLocationResult.SUCCESS;

        if(this.mockingThread != null) {
            this.mockingThread.stop();
        }

        try {
            this.geo.unmockLocation(this.activity);
        } catch (Exception e) {
            e.printStackTrace();
            return MockLocationResult.FAIL;
        }

        return result;
    }
}
