package com.fgps.controllers;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.fgps.models.SettingsList;

public class SettingsGenerationController {
    private final Context context;
    private final TableLayout layout;
    private final SettingsList settingsList;

    public SettingsGenerationController(Context context, TableLayout tableLayout, SettingsList settingsList) {
        this.context = context;
        this.layout = tableLayout;
        this.settingsList = settingsList;
    }

    public void generateTable() {
        int size = this.settingsList.getSize();

        for(int i = 0; i < size; i++) {
            TableRow[] rows = generateRow(
                    this.settingsList.getName(i),
                    this.settingsList.getValue(i),
                    this.settingsList.getListener(i)
            );

            addRow(rows[0]);
            addRow(rows[1]);

            if(i < size - 1) {
                addRow(generateSplitter());
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void addRow(TableRow tableRow) {
        this.layout.addView(tableRow, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }

    @SuppressWarnings("deprecation")
    private TableRow[] generateRow(final String name, final String value, final View.OnClickListener listener) {
        TableRow titleRow = new TableRow(this.context);
        TableRow valueRow = new TableRow(this.context);

        titleRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        valueRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView titleView = new TextView(this.context);
        TextView valueView = new TextView(this.context);

        titleView.setTextSize(20);
        valueView.setTextSize(14);

        titleView.setText(name);
        valueView.setText(value);

        titleView.setPadding(20, 5, 20, 5);
        valueView.setPadding(20, 0, 20, 10);

        titleView.setTextColor(Color.WHITE);
        valueView.setTextColor(Color.rgb(170, 170, 170));

        titleRow.addView(titleView);
        valueRow.addView(valueView);

        titleRow.setOnClickListener(listener);
        valueRow.setOnClickListener(listener);

        return new TableRow[] { titleRow, valueRow };
    }

    @SuppressWarnings("deprecation")
    private TableRow generateSplitter() {
        TableRow splitter = new TableRow(this.context);

        splitter.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        splitter.setPadding(0, 1, 0, 0);
        splitter.setBackgroundColor(Color.rgb(36, 41, 48));

        return splitter;
    }
}
