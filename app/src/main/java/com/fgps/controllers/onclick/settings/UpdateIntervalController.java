package com.fgps.controllers.onclick.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import com.fgps.R;
import com.fgps.controllers.sqlite.SettingsController;
import com.fgps.models.Settings;
import com.fgps.models.geo.MockingLocationRunnable;

public class UpdateIntervalController implements View.OnClickListener {
    private final Context context;
    private final Resources resources;

    public UpdateIntervalController(Context context, Resources resources) {
        this.context = context;
        this.resources = resources;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(this.resources.getString(R.string.update_interval));

        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(MockingLocationRunnable.interval));

        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int interval = Integer.parseInt(input.getText().toString());

                    if(interval >= 100 && interval <= 120000) {
                        MockingLocationRunnable.interval = interval;
                        SettingsController settingsController = new SettingsController(context);

                        MockingLocationRunnable.interval =
                                Integer.parseInt(settingsController.get(Settings.UPDATE_INTERVAL.toString()));
                    }
                } catch (NumberFormatException ignored) { }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
