package com.fgps.controllers.onclick.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.view.View;
import android.app.AlertDialog;
import android.widget.EditText;
import com.fgps.R;
import com.fgps.models.geo.Geo;

public class AccuracyController implements View.OnClickListener {
    private final Context context;
    private final Activity activity;
    private final Resources resources;

    public AccuracyController(Context context, Activity activity, Resources resources) {
        this.context = context;
        this.activity = activity;
        this.resources = resources;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(this.resources.getString(R.string.accuracy));

        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(Geo.accuracy));

        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Geo geo = new Geo();

                try {
                    int accuracy = Integer.parseInt(input.getText().toString());

                    if(geo.testAccuracy(activity, accuracy)) {
                        Geo.accuracy = accuracy;
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
