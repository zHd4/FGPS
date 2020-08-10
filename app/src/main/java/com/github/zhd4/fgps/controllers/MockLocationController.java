package com.github.zhd4.fgps.controllers;

import android.app.Activity;
import android.content.Context;
import com.github.zhd4.fgps.models.geo.Coordinates;
import com.github.zhd4.fgps.models.geo.Geo;
import com.github.zhd4.fgps.models.sqlite.Connector;
import com.github.zhd4.fgps.models.sqlite.locations.tables.TableLast;

public class MockLocationController {
    private final Geo geo;
    private final Context context;
    private final Activity activity;

    public MockLocationController(Context context, Activity activity, Geo geo) {
        this.context = context;
        this.activity = activity;
        this.geo = geo;
    }

    public boolean isMockRunning() {
        Connector sqliteConnector = new Connector(context);
        TableLast table = new TableLast(sqliteConnector.getWritableDatabase());

        boolean isEmpty = table.isEmpty();
        table.close();

        return !isEmpty;
    }

    public MockLocationResult startMock(Coordinates newCoordinates) {
        MockLocationResult result = MockLocationResult.SUCCESS;

        Connector sqliteConnector = new Connector(context);
        TableLast table = new TableLast(sqliteConnector.getWritableDatabase());

        if(geo.mockLocation(activity, newCoordinates)) {
            table.setCoordinates(geo.getCurrentLocation(activity, context));
        } else {
            result = MockLocationResult.FAIL;
        }

        table.close();

        return result;
    }

    public MockLocationResult stopMock() {
        MockLocationResult result = MockLocationResult.SUCCESS;

        Connector sqliteConnector = new Connector(context);
        TableLast table = new TableLast(sqliteConnector.getWritableDatabase());

        Coordinates lastCoords = table.getCoordinates();

        if(lastCoords != null) {
            if(geo.mockLocation(activity, lastCoords)) {
                table.clear();
            } else {
                result = MockLocationResult.FAIL;
            }
        } else {
            result = MockLocationResult.IGNORE;
        }

        table.close();

        return result;
    }
}
