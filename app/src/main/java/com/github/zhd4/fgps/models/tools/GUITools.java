package com.github.zhd4.fgps.models.tools;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;

public class GUITools {
    public static void showMessage(Activity activity,  String text) {
        new AlertDialog.Builder(activity).setTitle("").setMessage(text).show();
    }
}
