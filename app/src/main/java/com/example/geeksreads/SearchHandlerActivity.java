package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;

public class SearchHandlerActivity extends AppCompatActivity  {
    RecyclerView recyclerViewSearchHandler;
    RecyclerView.Adapter adapter;
    private List<SearchHandlerItem> list;
    TextView moreSearchResults;
    Context mContext;
    int end=1;
    /**
     * @param savedInstanceState
     * Overrided Function to decide what will appear after starting this Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_handler);
        recyclerViewSearchHandler = findViewById(R.id.SearchHandlerRecycler);
        recyclerViewSearchHandler.setHasFixedSize(true);
        recyclerViewSearchHandler.setLayoutManager(new LinearLayoutManager(this));
        mContext = this;
        moreSearchResults = findViewById(R.id.MoreResultsOfSearchText);
        list=new ArrayList<>();

        Toolbar myToolbar = findViewById(R.id.toolbar);
         setSupportActionBar(myToolbar);
         Intent i =getIntent();
         String S=i.getStringExtra("FirstSearch");
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Want to Read");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SearchHandlerItem o = new SearchHandlerItem(
                S,
                "Author",
                "ID"
        );

        list.add(o);
        S="";
        //}
        end++;
        adapter =new SearchHandlerAdapter(list,getApplicationContext());
        recyclerViewSearchHandler.setAdapter(adapter);


    }

    /**
     * @param menu : Toolbar menu Object
     * @return super.onCreateOptionsMenu(menu)
     *  Overrided Function to create the toolbar and decide what to do when click it's menu items.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(800);
        searchView.setQueryHint("Search books");
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(800);
        searchView.setQueryHint("Search books");
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        searchView.setFocusable(true);
        searchView.setFocusable(true);
        searchView.setTouchscreenBlocksFocus(true);
        searchView.setEnabled(true);
        searchView.setKeepScreenOn(true);
        searchView.setKeyboardNavigationCluster(true);

        //Intent i =getIntent();
        //String S=i.getStringExtra("FirstSearch");
       // searchView.setQuery((CharSequence) i,false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent mIntent = new Intent(SearchHandlerActivity.this, SearchResultActivity.class);
                startActivity(mIntent);
                Log.e("queryText",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String S=new String();

                //for(int i=0;i<=end;i++)
                //{
                    S+=end;
                    S+="+";
                  //  S+=i;
                    SearchHandlerItem o = new SearchHandlerItem(
                             searchView.getQuery().toString(),
                            "Author",
                            "ID"
                    );

                    list.add(o);
                    S="";
                //}
                end++;
                adapter =new SearchHandlerAdapter(list,getApplicationContext());
                recyclerViewSearchHandler.setAdapter(adapter);

                Log.e("queryTextChange",newText);
                return true;
            }


        });
        MenuItem item1 = menu.findItem(R.id.NotificationButton);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mIntent = new Intent(SearchHandlerActivity.this, NotificationActivity.class);
                startActivity(mIntent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }







    public class SearchHandlerData extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
        JSONObject mJSON = new JSONObject();

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            String result = "";

            try {
                /* Create a URL object holding our url */
                URL url = new URL(UrlString);
                /* Create an HTTP Connection and adjust its options */
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("content-type", "application/json");

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                //String data = URLEncoder.encode(JSONString, "UTF-8");
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
                        result = "{\"ReturnMsg\":\"Invalid email or password.\"}";
                        break;
                    case "401":
                        result = "{\"ReturnMsg\":\"Your account has not been verified.\"}";
                        break;
                    default:
                        break;
                }


                http.disconnect();
                return result;

            }
            /* Handling Exceptions */ catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                /* Creating a JSON Object to parse the data in */
                final JSONObject jsonObject = new JSONObject(result);
                JSONArray array1 = jsonObject.getJSONArray(result);
                JSONArray array=new JSONArray(result);
                int end = array.length() < 4 ? array.length() : 4;
                for (int i = 0; i < end; i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    SearchHandlerItem s = new SearchHandlerItem(
                            o.getString("BookName"),
                            o.getString("AuthorName"),
                            o.getString("BookId")

                    );

                }

            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
