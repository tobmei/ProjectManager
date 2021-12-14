package com.example.projectmanager.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.projectmanager.Controller.ProjectDetailActivityListener;
import com.example.projectmanager.R;

public class ProjectDetailActivity extends AppCompatActivity {

    ProjectDetailActivityListener projectDetailActivityListener;

    public TextView txtvProjectName, txtvProjectDetailCustomer, txtvProjectDetailCreateDate, txtvTimeWorked, txtvProjectDetailDescription;

    ImageButton btnNewTimer, btnShowTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        txtvProjectName = findViewById(R.id.txtvProjectDetailName);
        txtvProjectDetailCustomer = findViewById(R.id.txtvProjectDetailCustomer);
        txtvProjectDetailCreateDate = findViewById(R.id.txtvProjectDetailCreateDate);
        txtvTimeWorked = findViewById(R.id.txtvTimeWorked);
        txtvProjectDetailDescription = findViewById(R.id.txtvProjectDetailDescription);

        btnNewTimer = findViewById(R.id.btnNewTask);
        btnShowTasks = findViewById(R.id.btnShowTasks);

        projectDetailActivityListener = new ProjectDetailActivityListener(this);

        btnNewTimer.setOnClickListener(projectDetailActivityListener);
        btnShowTasks.setOnClickListener(projectDetailActivityListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu_detail_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return projectDetailActivityListener.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        projectDetailActivityListener.onResume();
    }
}