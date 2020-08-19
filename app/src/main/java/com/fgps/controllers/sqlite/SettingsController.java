package com.fgps.controllers.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsController extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "settings";
    private static final String TABLE_VALUES = "settings_values";

    private static final String KEY_NAME = "settings_name";
    private static final String KEY_VALUE = "settings_value";

    public SettingsController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s TEXT, %s TEXT)", TABLE_VALUES, KEY_NAME, KEY_VALUE));
    }

    @SuppressLint("Recycle")
    public String get(String name) {
        String value = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                String.format("SELECT %s FROM %s WHERE %s='%s'", KEY_VALUE, TABLE_VALUES, KEY_NAME, name),
                null);

        if(cursor.getCount() > 0 && cursor.moveToFirst()) {
            value = cursor.getString(0);
        }

        return value;
    }

    public void set(String name, String value) {
        SQLiteDatabase db = getWritableDatabase();

        if(get(name) != null) {
            db.execSQL(String.format("UPDATE %s SET %s='%s' WHERE %s='%s'",
                    TABLE_VALUES, KEY_VALUE, value, KEY_NAME, name));
        } else {
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_NAME, name);
            contentValues.put(KEY_VALUE, value);

            db.insert(TABLE_VALUES, null ,contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}
