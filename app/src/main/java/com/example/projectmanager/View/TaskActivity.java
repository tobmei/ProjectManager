package com.example.projectmanager.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.projectmanager.Controller.TaskActivityListener;
import com.example.projectmanager.R;

public class TaskActivity extends AppCompatActivity {

    private static final String TAG = TaskActivity.class.getSimpleName();

    TaskActivityListener taskActivityListener;
    public ImageButton btnStartPause;
    public Button btnEnd;
    public TextView txtvClock, txtvProjectName, txtvTaskDescription;
    MenuItem cmDelete;

    static boolean active = false;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_clock);

        txtvClock = findViewById(R.id.txtvClock);
        txtvProjectName = findViewById(R.id.txtvTaskDetailProjectName);
        txtvTaskDescription = findViewById(R.id.txtvTaskDetailDescription);

        btnStartPause = findViewById(R.id.btnStartPause);
        btnStartPause.setBackgroundResource(android.R.drawable.ic_media_play);
        btnEnd = findViewById(R.id.btnEnd);

        taskActivityListener = new TaskActivityListener(this);

        btnStartPause.setOnClickListener(taskActivityListener);
        btnEnd.setOnClickListener(taskActivityListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return taskActivityListener.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskActivityListener.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskActivityListener.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskActivityListener.onDestroy();
    }
}