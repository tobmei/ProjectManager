package com.example.projectmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class StopClockService extends Service {

    private static final String TAG = StopClockService.class.getSimpleName();

    private Thread counterThread;

    private boolean isRunning;
    private long startTime;
    private long elapsedTime;
    private long pausedAtTime;
    private long pausedDuration = 0;
    private Thread stopClockThread;

    public static final String ACTION_STOPCLOCK = "com.examples.projectmanager.stopclock";
    private Intent broadcastIntent = new Intent(ACTION_STOPCLOCK);

    private final IBinder iBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public StopClockService getService() {
            return StopClockService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"Service onCreate()");
        super.onCreate();
        stopClockThread = new Thread(counter);
        isRunning = false;
    }

    public void start(){
        if(Thread.State.NEW.equals(stopClockThread.getState())) {
            startTime = System.currentTimeMillis();
            stopClockThread.start();
        }
        else
            pausedDuration += System.currentTimeMillis() - pausedAtTime;

        isRunning = true;
    }

    public void pause(){
        isRunning = false;
        pausedAtTime = System.currentTimeMillis();
    }

    public void killThread(){
        if(stopClockThread != null && stopClockThread.isAlive())
            stopClockThread.interrupt();
        stopClockThread = null;
    }

    private Runnable counter = new Runnable() {
        @Override
        public void run() {
            while(true) {
                if (isRunning){
                    elapsedTime = System.currentTimeMillis() - startTime - pausedDuration;
                    broadcastIntent.putExtra("elapsedTime", elapsedTime);
                    sendBroadcast(broadcastIntent);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public boolean isRunning(){
        return isRunning;
    }

    public boolean isAlive(){
        return stopClockThread.isAlive();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"Service onDestroy()");
        super.onDestroy();
        killThread();
    }
}
