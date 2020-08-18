package com.fgps.models.tools;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class GUITools {
    public static void showMessage(Activity activity,  String text) {
        new AlertDialog.Builder(activity).setTitle("").setMessage(text).show();
    }

    public static void showToast(String text, Context context) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
