package com.fgps.models;

import android.view.View;
import com.fgps.R;
import android.content.res.Resources;
import com.fgps.controllers.AccuracySettingsOnClickController;

import java.util.Arrays;

public class SettingsList {
    private String[][] settings;
    private View.OnClickListener[] settingsListeners;

    private final Resources resources;

    public SettingsList(Resources resources) {
        this.settings = new String[0][];
        this.settingsListeners = new View.OnClickListener[0];
        this.resources = resources;
        initArray();
    }

    public int getSize() {
        return this.settings.length;
    }

    public String getName(int index) {
        return this.settings[index][0];
    }

    public String getValue(int index) {
        return this.settings[index][1];
    }

    public View.OnClickListener getListener(int index) {
        return this.settingsListeners[index];
    }

    private void initArray() {
        add(
                this.resources.getString(R.string.accuracy),
                this.resources.getString(R.string.set_gps_fix_accuracy_value_in_meters),
                new AccuracySettingsOnClickController()
        );
    }

    private void add(final String name, final String value, final View.OnClickListener listener) {
        this.settings = Arrays.copyOf(this.settings, this.settings.length + 1);
        this.settings[this.settings.length - 1] = new String[] { name, value };

        this.settingsListeners = Arrays.copyOf(this.settingsListeners, this.settingsListeners.length + 1);
        this.settingsListeners[this.settingsListeners.length - 1] = listener;
    }
}
