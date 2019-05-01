package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class NotificationService extends Service {

    private final Context context;

    public NotificationService() {
        this.context = this;
    }

    void showPushNotification(){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", UserSessionManager.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String UrlService = APIs.API_GET_USER_NOTIFICATIONS;

        GetNotificationsList performBackgroundTask = new GetNotificationsList();
        performBackgroundTask.execute(UrlService, jsonObject.toString());

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

    /**
     * A Private class that extend Async Task to connect to server in background.
     * It get user Notifications lists.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetNotificationsList extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        boolean TaskSuccess;

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            String result = "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("content-type", "application/json");

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));

                writer.write(JSONString);
                writer.flush();
                writer.close();
                ops.close();

                Log.d("ResponseCode: " , String.valueOf(http.getResponseCode()) );
                if (String.valueOf(http.getResponseCode()).equals("200")) {
                    /* A Stream object to get the returned data from API Call */
                    InputStream ips = http.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                    reader.close();
                    ips.close();
                    TaskSuccess = true;
                }
                else {
                    TaskSuccess = false;
                    InputStream es = http.getErrorStream();
                    BufferedReader ereader = new BufferedReader(new InputStreamReader(es, StandardCharsets.ISO_8859_1));
                    String eline;
                    while ((eline = ereader.readLine()) != null) {
                        result += eline;
                    }
                    ereader.close();
                    es.close();
                }
                http.disconnect();
                return result;

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            //progress.setVisibility(View.GONE);

            if (result == null) {
                return;
            }
            try {

                JSONArray Notifications = new JSONArray(result);
                for (int i=0 ; i< Notifications.length(); i++)
                {
                    JSONObject CurrentNotification = Notifications.getJSONObject(i);
                    String body = CurrentNotification.getString("body");
                    if (!CurrentNotification.getBoolean("seen")) {
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notify = new Notification.Builder(context.getApplicationContext()).setContentTitle("GeeksRead").setContentText(body).setContentTitle("GeeksReads").setSmallIcon(R.drawable.ic_book).build();
                        notify.defaults |= Notification.DEFAULT_SOUND;
                        notify.defaults |= Notification.DEFAULT_VIBRATE;
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                        Objects.requireNonNull(notificationManager).notify(0, notify);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}