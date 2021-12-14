package com.example.projectmanager.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.projectmanager.R;
import com.example.projectmanager.Controller.MainActivityListener;

public class MainActivity extends AppCompatActivity {

    MainActivityListener mainActivityListener;

    ImageButton imgbtnCusotmers, imgbtnProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgbtnCusotmers = findViewById(R.id.imgbtnCustomers);
        imgbtnProjects = findViewById(R.id.imgbtnProjects);

        mainActivityListener = new MainActivityListener(this);

        imgbtnCusotmers.setOnClickListener(mainActivityListener);
        imgbtnProjects.setOnClickListener(mainActivityListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return mainActivityListener.onOptionsItemSelected(item);
    }
}