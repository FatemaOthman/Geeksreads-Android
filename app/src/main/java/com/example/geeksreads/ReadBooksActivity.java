package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geeksreads.views.LoadingView;

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

public class ReadBooksActivity extends AppCompatActivity {

    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONArray data = new JSONArray();
    Context mContext;
    View rootView;
    LoadingView Loading;
    /**
     * @param savedInstanceState
     * Overrided Function to decide what will appear after starting this Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_books);
        mContext = this;

        Loading = new LoadingView(null, (FrameLayout)findViewById(R.id.progressBarHolder), (TextView)findViewById(R.id.ProgressName));

        Toolbar myToolbar = findViewById(R.id.toolbar);
        rootView=findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Read");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", UserSessionManager.getUserToken());
            jsonObject.put("UserId", UserSessionManager.getUserID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String UrlService = APIs.API_GET_READ_LIST;

        mSwipeRefreshLayout = findViewById(R.id.ReadSwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /**
             *  Overrided Function to decide what to do when refreshing the layout.
             */
            @Override
            public void onRefresh() {
                GetReadBooks performBackgroundTask = new GetReadBooks();
                performBackgroundTask.execute(UrlService, jsonObject.toString());
            }
        });

        GetReadBooks performBackgroundTask = new GetReadBooks();
        Log.d("Body", jsonObject.toString());
        performBackgroundTask.execute(UrlService, jsonObject.toString());
        
    }

    /**
     * @param menu : Toolbar menu Object
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
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                rootView.requestFocus();
                Intent intent = new Intent(ReadBooksActivity.this,SearchHandlerActivity.class);
                intent.putExtra("CallingActivity","ReadBooksActivity");
                startActivity(intent);

            }
        });

        MenuItem item1 = menu.findItem(R.id.NotificationButton);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mIntent = new Intent(ReadBooksActivity.this, NotificationActivity.class);
                startActivity(mIntent);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A Private class that extend Async Task to connect to server in background.
     * It get the Read book lists.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetReadBooks extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        AlertDialog dialog;
        boolean TaskSuccess = false;

        @Override
        protected void onPreExecute() {
            Loading.Start("Loading, Please wait...");
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

                //Create a new InputStreamReader
                Log.d("Code:" ,String.valueOf(http.getResponseCode()));
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
                else                {
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
            mSwipeRefreshLayout.setRefreshing(false);
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage(result);
                Log.d("result:", result);
                //dialog.show();
                JSONObject jsonObject = new JSONObject(result);
                if (TaskSuccess) {
                    ListView readBookList = findViewById(R.id.ReadBookList);
                    final BookList_JSONAdapter bookListJsonAdapter = new BookList_JSONAdapter(mContext, new JSONArray(jsonObject.getString("ReadData")));
                    readBookList.setAdapter(bookListJsonAdapter);
                    readBookList.deferNotifyDataSetChanged();
                    readBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            Intent intent = new Intent(ReadBooksActivity.this, BookActivity.class);
                            intent.putExtra("BookID", bookListJsonAdapter.getBookID(position));
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    Toast.makeText(mContext, "Error happened during loading list ", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Loading.Stop();
        }

    }

}
