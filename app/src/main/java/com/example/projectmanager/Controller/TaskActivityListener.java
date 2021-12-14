package com.example.projectmanager.Controller;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.example.projectmanager.DataSource;
import com.example.projectmanager.R;
import com.example.projectmanager.StopClockService;
import com.example.projectmanager.View.TaskActivity;
import com.example.projectmanager.Utility;
import com.example.projectmanager.model.Project;
import com.example.projectmanager.model.Task;

public class TaskActivityListener implements View.OnClickListener {

    private static final String TAG = TaskActivity.class.getSimpleName();

    private boolean isBound;
    private StopClockService stopClockService;

    private Intent intent;
    private Project project;
    private String description;
    private int secondsWorked;
    private boolean pauseOnCall = false;

    private TelephonyManager telephonyManager;

    SharedPreferences sharedPreferences;

    DataSource dataSource;
    TaskActivity taskActivity;

    ActionBar actionBar;


    public TaskActivityListener(TaskActivity taskActivity) {
        this.taskActivity = taskActivity;

        actionBar = taskActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        taskActivity.getSupportActionBar().setTitle(R.string.text_new_task);

        dataSource = new DataSource(taskActivity);

        sharedPreferences = taskActivity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("PauseInCall")){
            pauseOnCall = sharedPreferences.getBoolean("PauseInCall", false);
        }

        telephonyManager = (TelephonyManager) taskActivity.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        project = taskActivity.getIntent().getParcelableExtra("project");
        description = taskActivity.getIntent().getStringExtra("description");

        taskActivity.txtvProjectName.setText(project.getName());
        taskActivity.txtvTaskDescription.setText(description);

        secondsWorked = 0;

        bindToService();
    }


    //region Service management

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            stopClockService = ((StopClockService.LocalBinder) service).getService();
            Log.d(TAG, "Connection to Service established");
        }
        public void onServiceDisconnected(ComponentName className) {
            stopClockService = null;
            Log.d(TAG, "Unexpectedly disconnected from Service. This should never happen!");
        }
    };

    void bindToService() {
        intent = new Intent(taskActivity, StopClockService.class);
        isBound = taskActivity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (isBound)
            Log.d(TAG, "Service successfully bound");
        else
            Log.d(TAG, "Service not bound");
    }

    void unbindFromService() {
        if (isBound) {
            taskActivity.unbindService(connection);
            isBound = false;
            Log.d(TAG, "Unbound from Service");
        }
    }
    //endregion

    //region Receiver
    //Empfängt die verstrichene Zeit vom StopClock Service
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                long elapsedTime = intent.getLongExtra("elapsedTime", 0);
                secondsWorked = (int) elapsedTime / 1000;
                String formattedTime = Utility.getFormattedTime(secondsWorked); //reuturn time in 00:00:00 format
                taskActivity.txtvClock.setText(formattedTime);
            }
        }
    };

    // Horcht nach angenommenen Anrufen
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pauseOnCall)
                        pauseStopClock();
                    break;
            }
        }
    };
    //endregion

    private void pauseStopClock(){
        if(stopClockService.isRunning()) {
            stopClockService.pause();
            taskActivity.btnStartPause.setBackgroundResource(android.R.drawable.ic_media_play);
        }
        else{
            stopClockService.start();
            taskActivity.btnStartPause.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnStartPause:
                pauseStopClock();
                break;
            case R.id.btnEnd:
                if(stopClockService.isRunning()) {
                    stopClockService.pause();
                    taskActivity.btnStartPause.setBackgroundResource(android.R.drawable.ic_media_play);
                }
                newTaskEndConfirmationDialog();
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            if(isBound && stopClockService.isAlive()) {
                stopClockService.pause();
                taskActivity.btnStartPause.setBackgroundResource(android.R.drawable.ic_media_play);
                newTaskEndConfirmationDialog();
            }
            else {
                unbindFromService();
                taskActivity.finish();
            }
        return true;
    }

    private void newTaskEndConfirmationDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(taskActivity);
        alert.setTitle("Zeiterfassung Beenden")
                .setMessage("Wollen Sie die Zeiterfassung beenden und speichern?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Task task = new Task(project.getDbID(), description, Utility.getCurrentTime(), secondsWorked);
                        dataSource.insertTask(task);
                        stopClockService.killThread();
                        unbindFromService();
                        taskActivity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    public void onResume() {
        taskActivity.registerReceiver(broadcastReceiver,
                new IntentFilter(StopClockService.ACTION_STOPCLOCK));
    }

    public void onStop(){
        taskActivity.unregisterReceiver(broadcastReceiver);
    }

    public void onDestroy() {
        unbindFromService();
    }

}
