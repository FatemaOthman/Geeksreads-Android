package com.example.geeksreads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class NotificationService extends Service {

    void showPushNotification(){
            }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
            for (int i = 0; i < 5; i++) {
                long futureTime = System.currentTimeMillis() + 15000;
                while (System.currentTimeMillis() < futureTime) {
                    synchronized (this) {
                        try {
                            wait(futureTime - System.currentTimeMillis());
                            showPushNotification();
                        } catch (InterruptedException e) {
                            e.getMessage();
                        }
                    }
                }
            }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}