package com.fgps.controllers.onclick;

import com.fgps.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.fgps.controllers.MockLocationController;
import com.fgps.controllers.MockLocationResult;
import com.fgps.models.tools.GUITools;
import com.fgps.models.geo.Geo;
import com.fgps.views.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ToggleGpsController implements View.OnClickListener {
    private final Geo geo;
    private final Context context;
    private final MainActivity activity;
    private final FloatingActionButton toggleGpsButton;

    private boolean running;

    public ToggleGpsController(Geo geo, Context context, MainActivity mainActivity,
                               FloatingActionButton toggleGpsButton) {
        this.geo = geo;
        this.context = context;
        this.activity = mainActivity;
        this.toggleGpsButton = toggleGpsButton;
    }

    @Override
    public void onClick(View view) {
        MockLocationController mockController = new MockLocationController(activity, geo);

        if(!this.running) {
            MockLocationResult startMockResult = mockController.startMock(activity.coordinates);

            if(startMockResult.equals(MockLocationResult.SUCCESS)) {
                this.running = true;
                this.toggleGpsButton.setImageResource(android.R.drawable.ic_media_pause);
                this.toggleGpsButton.setColorFilter(Color.rgb(255, 64, 64));

                GUITools.showToast(activity.getResources().getString(R.string.mockingAt) + " " +
                        activity.coordinates.getLatitude() + ", " + activity.coordinates.getLongitude(), this.context);
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
