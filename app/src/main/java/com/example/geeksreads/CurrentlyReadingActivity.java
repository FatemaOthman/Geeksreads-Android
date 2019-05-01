package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class CurrentlyReadingActivity extends AppCompatActivity {

    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONArray data = new JSONArray();
    Context mContext;

    /**
     * @param savedInstanceState
     * Overrided Function to decide what will appear after starting this Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currently_reading);
        mContext = this;

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Currently Reading");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", UserSessionManager.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String UrlService = "https://geeksreads.herokuapp.com/api/Users/GetUserReadingDetails";

        mSwipeRefreshLayout = findViewById(R.id.CurrentlySwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /**
             *  Overrided Function to decide what to do when refreshing the layout.
             */
            @Override
            public void onRefresh() {
                GetCurrentlyReadingBooks performBackgroundTask = new GetCurrentlyReadingBooks();
                performBackgroundTask.execute(UrlService, jsonObject.toString());
            }
        });

        GetCurrentlyReadingBooks performBackgroundTask = new GetCurrentlyReadingBooks();
        performBackgroundTask.execute(UrlService,jsonObject.toString());

    }

    /**
     * @param menu Toolbar menu Object
     * @return super.onCreateOptionsMenu(menu)
     *  Overrided Function to create the toolbar and decide what to do when click it's menu items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSetting);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(800);
        searchView.setQueryHint("Search books");
        MenuItem item1 = menu.findItem(R.id.NotificationButton);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mIntent = new Intent(CurrentlyReadingActivity.this, NotificationActivity.class);
                startActivity(mIntent);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A Private class that extend Async Task to connect to server in background.
     * It get the Currently Reading book lists.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetCurrentlyReadingBooks extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
            //progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            String result = "";

            try {
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("content-type", "application/json");

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                //String data = URLEncoder.encode("Json", "UTF-8") + "=" + URLEncoder.encode(JSONString, "UTF-8");
                String data = JSONString;
                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line = "";
                        //boolean started = false;
                        while ((line = reader.readLine()) != null) {
                            //   if ()
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    case "400":
                        InputStream es = http.getErrorStream();
                        BufferedReader ereader = new BufferedReader(new InputStreamReader(es, StandardCharsets.ISO_8859_1));
                        String eline = "";
                        while ((eline = ereader.readLine()) != null) {
                            result += eline;
                        }
                        break;
                    default:
                        break;
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
            mSwipeRefreshLayout.setRefreshing(false);
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage(result);
                //dialog.show();
                JSONObject jsonObject = new JSONObject(result);
                ListView currentlyReadingList = findViewById(R.id.CurrentlyReadingList);
                final BookList_JSONAdapter bookListJsonAdapter = new BookList_JSONAdapter(mContext, new JSONArray(jsonObject.getString("ReadingData")));
                currentlyReadingList.setAdapter(bookListJsonAdapter);
                currentlyReadingList.deferNotifyDataSetChanged();
                currentlyReadingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Intent intent = new Intent(CurrentlyReadingActivity.this, BookActivity.class);
                        intent.putExtra("BookID",bookListJsonAdapter.getBookID(position));
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                Toast.makeText(mContext, "Unable to get Shelf Data from server", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }
}
