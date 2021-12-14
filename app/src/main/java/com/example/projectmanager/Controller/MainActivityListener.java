package com.example.projectmanager.Controller;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.example.projectmanager.R;
import com.example.projectmanager.View.ListViewActivity;
import com.example.projectmanager.View.MainActivity;
import com.example.projectmanager.View.SettingsActivity;

public class MainActivityListener implements View.OnClickListener {

    MainActivity mainActivity;

    public MainActivityListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;

        mainActivity.getSupportActionBar().setTitle(R.string.dashboard);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mainActivity, ListViewActivity.class);
        switch(v.getId()){
            case R.id.imgbtnCustomers:
                intent.putExtra("mode", "customer");
                mainActivity.startActivity(intent);
                break;
            case R.id.imgbtnProjects:
                intent.putExtra("mode", "project_all");
                mainActivity.startActivity(intent);
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.omSettings){
            mainActivity.startActivity(new Intent(mainActivity, SettingsActivity.class));
        }
        return true;
    }
}
