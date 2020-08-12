package com.github.zhd4.fgps.controllers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.github.zhd4.fgps.models.tools.MainActivityTools;
import com.google.android.gms.maps.GoogleMap;

public class EditTextOnChangedController implements TextWatcher {
    private static boolean ignoring = false;

    private final EditText latitude;
    private final EditText longitude;
    private final GoogleMap googleMap;

    public EditTextOnChangedController(EditText latitude, EditText longitude, GoogleMap googleMap) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.googleMap = googleMap;
    }

    public static void setIgnoring(boolean ignoring) {
        EditTextOnChangedController.ignoring = ignoring;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            if(!ignoring) {
                MainActivityTools tools = new MainActivityTools();
                tools.setPointOnMap(googleMap, latitude, longitude);
            }
        } catch (NumberFormatException ignore) { }
    }
}
