package com.example.projectmanager.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.example.projectmanager.R;
import com.example.projectmanager.Controller.SettingsActivityListener;

public class SettingsActivity extends AppCompatActivity {

    public CheckBox cbCheck, cbReadPhone;
    SharedPreferences sharedPreferences;

    SettingsActivityListener settingsActivityListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cbCheck = findViewById(R.id.cbCheck);
        cbReadPhone = findViewById(R.id.cbReadPhone);

        settingsActivityListener = new SettingsActivityListener(this);

        cbReadPhone.setOnCheckedChangeListener(settingsActivityListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        settingsActivityListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        settingsActivityListener.onOptionsItemSelected(item);
        return true;
    }
}