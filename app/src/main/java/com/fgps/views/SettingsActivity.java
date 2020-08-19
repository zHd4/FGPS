package com.fgps.views;

import android.widget.TableLayout;
import com.fgps.R;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.fgps.models.SettingsList;

public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.actionSettings);

        new SettingsMenuGenerator(
                getApplicationContext(),
                this.<TableLayout>findViewById(R.id.settingsTableLayout),
                new SettingsList(SettingsActivity.this, SettingsActivity.this, getResources())
        ).generateTable();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
