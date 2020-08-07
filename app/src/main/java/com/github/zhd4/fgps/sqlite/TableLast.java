package com.github.zhd4.fgps.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.github.zhd4.fgps.geo.Coordinates;

public class TableLast implements ITable {
    private static final String TABLE_LAST = "last";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private final SQLiteDatabase writableDatabase;

    public TableLast(SQLiteDatabase writableDatabase) {
        this.writableDatabase = writableDatabase;

        createTable();
    }

    public Coordinates getCoordinates() {
        double latitude = 0;
        double longitude = 0;

        try {
            @SuppressLint("Recycle")
            Cursor cursor = writableDatabase.rawQuery(String.format("SELECT * FROM %s", TABLE_LAST), null);

            if (cursor.moveToFirst()) {
                latitude = Double.parseDouble(cursor.getString(0));
                longitude = Double.parseDouble(cursor.getString(1));
            }

            return new Coordinates(latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public void setCoordinates(Coordinates coordinates) {
        ContentValues values = new ContentValues();

        values.put(KEY_LATITUDE, coordinates.getLatitude());
        values.put(KEY_LONGITUDE, coordinates.getLongitude());

        writableDatabase.insert(TABLE_LAST, null, values);
    }

    @Override
    public void createTable() {
        writableDatabase.execSQL(
                String.format("CREATE TABLE IF NOT EXISTS %s(%s TEXT, %s TEXT)",
                        TABLE_LAST,
                        KEY_LATITUDE,
                        KEY_LONGITUDE)
        );
    }

    @Override
    public void clearTable() {
        writableDatabase.execSQL(String.format("DELETE FROM %s", TABLE_LAST));
    }

    @Override
    public void closeTable() {
        writableDatabase.close();
    }
}
