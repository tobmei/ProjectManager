package com.example.projectmanager.Controller;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;

import com.example.projectmanager.R;
import com.example.projectmanager.View.SettingsActivity;

public class SettingsActivityListener implements CompoundButton.OnCheckedChangeListener {

    SettingsActivity settingsActivity;
    SharedPreferences sharedPreferences;

    private static final String myPreferences = "settings";
    static final int REQUEST_READ_PHONE_STATE = 456;

    public SettingsActivityListener(SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;

        settingsActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        settingsActivity.getSupportActionBar().setTitle(R.string.settingsTitle);

        sharedPreferences = settingsActivity.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        if(sharedPreferences.contains("OnlyUndone"))
            settingsActivity.cbCheck.setChecked(sharedPreferences.getBoolean("OnlyUndone", false));
        if(sharedPreferences.contains("PauseInCall"))
            settingsActivity.cbReadPhone.setChecked(sharedPreferences.getBoolean("PauseInCall", false));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                settingsActivity.cbReadPhone.setChecked(true);
            else
                settingsActivity.cbReadPhone.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (settingsActivity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                settingsActivity.cbReadPhone.setChecked(true);
            else
                ActivityCompat.requestPermissions(settingsActivity, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_READ_PHONE_STATE);
        }
    }

    public void onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("OnlyUndone", settingsActivity.cbCheck.isChecked());
            editor.putBoolean("PauseInCall", settingsActivity.cbReadPhone.isChecked());
            editor.apply();
            settingsActivity.finish();
        }
    }
}
