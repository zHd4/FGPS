package com.github.zhd4.fgps.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import com.github.zhd4.fgps.models.tools.GUITools;
import com.github.zhd4.fgps.R;
import com.github.zhd4.fgps.models.geo.Coordinates;
import com.github.zhd4.fgps.models.geo.Geo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ToggleGpsOnClickController implements View.OnClickListener {
    private final Geo geo;
    private final Context context;
    private final Activity activity;
    
    private final EditText latitude;
    private final EditText longitude;
    private final FloatingActionButton toggleGpsButton;

    private boolean running;

    public ToggleGpsOnClickController(Geo geo, Context context, Activity activity,
                                      EditText latitude, EditText longitude, FloatingActionButton toggleGpsButton) {
        this.geo = geo;
        this.context = context;
        this.activity = activity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.toggleGpsButton = toggleGpsButton;
    }

    @Override
    public void onClick(View view) {
        Coordinates coordinates = new Coordinates(
                Double.parseDouble(latitude.getText().toString()),
                Double.parseDouble(longitude.getText().toString())
        );

        MockLocationController mockController = new MockLocationController(activity, geo);

        if(!this.running) {
            MockLocationResult startMockResult = mockController.startMock(coordinates);

            if(startMockResult.equals(MockLocationResult.SUCCESS)) {
                this.running = true;
                this.toggleGpsButton.setImageResource(android.R.drawable.ic_media_pause);
                this.toggleGpsButton.setColorFilter(Color.rgb(255, 64, 64));

                GUITools.showToast(activity.getResources().getString(R.string.mockingAt) + " " +
                        coordinates.getLatitude() + ", " + coordinates.getLongitude(), this.context);
            } else if(startMockResult.equals(MockLocationResult.FAIL)) {
                GUITools.showMessage(this.activity, activity.getResources().getString(R.string.allowMockMessage));
            }
        } else {
            MockLocationResult stopMockResult = mockController.stopMock();

            if(stopMockResult.equals(MockLocationResult.SUCCESS)) {
                this.running = false;
                this.toggleGpsButton.setImageResource(android.R.drawable.ic_media_play);
                this.toggleGpsButton.setColorFilter(Color.rgb(255, 255, 255));

                GUITools.showToast(activity.getResources().getString(R.string.mockingStopped), this.context);
            } else if(stopMockResult.equals(MockLocationResult.FAIL)) {
                GUITools.showMessage(this.activity, activity.getResources().getString(R.string.allowMockMessage));
            }
        }
    }
}
