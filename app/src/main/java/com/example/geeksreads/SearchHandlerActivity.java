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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

import CustomFunctions.APIs;

public class SearchHandlerActivity extends AppCompatActivity  {
    RecyclerView recyclerViewSearchHandler;
    RecyclerView.Adapter adapter;
    private static String CallingActivity;
    private List<SearchHandlerItem> list;
    TextView moreSearchResults;
    Context mContext;

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
        CallingActivity=getIntent().getStringExtra("CallingActivity");

        Toolbar myToolbar = findViewById(R.id.toolbar);
         setSupportActionBar(myToolbar);
         Intent i =getIntent();
         String S=i.getStringExtra("FirstSearch");
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Want to Read");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent mIntent = new Intent(SearchHandlerActivity.this, SearchResultActivity.class);
                mIntent.putExtra("queryText",query);
                mIntent.putExtra("CallingActivity",CallingActivity);
                startActivity(mIntent);
                Log.e("queryText",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchHandlerDetails searchHandlerDetails = new SearchHandlerDetails();
                adapter = new SearchHandlerAdapter(list, getApplicationContext());
                ((SearchHandlerAdapter) adapter).clear();
                recyclerViewSearchHandler.setAdapter(adapter);
                list.clear();

                if(newText.length()>0) {
                    String UrlSearch = APIs.API_SEARCH;
                    searchHandlerDetails.execute(UrlSearch, newText);

                    Log.e("queryTextChange", newText);
                }
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
    @Override
    public void onBackPressed() {
        Intent intent;
        switch (CallingActivity) {

            case "AuthorActivity":
                 intent = new Intent(SearchHandlerActivity.this,
                        AuthorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "BookActivity":

                 intent = new Intent(SearchHandlerActivity.this,
                        BookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "ChangePasswordActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "CurrentlyReadingActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        CurrentlyReadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "EditProfileActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        EditProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "FeedActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        FeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case"MyBookShelvesActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        MyBooksShelvesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "NotificationActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;
            case"OtherProfileActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        OtherProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case"WantToReadActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        WantToReadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "Profile":
                intent = new Intent(SearchHandlerActivity.this,
                        Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case"ReadBooksActivity":
                intent = new Intent(SearchHandlerActivity.this,
                        ReadBooksActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            default:
                intent = new Intent(SearchHandlerActivity.this,
                        FeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);



        }

    }



    @SuppressLint("StaticFieldLeak")
    private class SearchHandlerDetails extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "GET";
        JSONObject mJSON = new JSONObject();

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String SearchQuery = params[1];
            String result = "";

            UrlString = UrlString + "?search_param=" + SearchQuery;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(UrlString);

            HttpResponse response = null;
            String server_response = null;
            try {
                response = httpclient.execute(httpget);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                try {
                    server_response = EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Server response", server_response);
            } else {
                Log.d("Server response", "Failed to get server response");
            }

            result = server_response;
            return result;        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                /* Creating a JSON Object to parse the data in */
                //final JSONObject jsonObject = new JSONObject(result);
                adapter = new SearchHandlerAdapter(list,getApplicationContext());
                ((SearchHandlerAdapter) adapter).clear();
                recyclerViewSearchHandler.setAdapter(adapter);

                JSONArray jsonArray=new JSONArray(result);
                JSONArray ArrayTitle = jsonArray.getJSONArray(0);

                for(int i=0;i<((2>ArrayTitle.length())?ArrayTitle.length():2);i++)
                {
                    JSONObject obj=ArrayTitle.getJSONObject(i);
                    SearchHandlerItem o = new SearchHandlerItem(
                            obj.getString("Title"),
                            obj.getString("AuthorName"),
                            obj.getString("BookId")
                    );

                    list.add(o);


                }
                if(jsonArray.length()>1)
                {
                    JSONArray ArrayAuthor = jsonArray.getJSONArray(1);
                    for(int i=0;i<((2>ArrayAuthor.length())?ArrayAuthor.length():2);i++)
                    {
                        JSONObject obj=ArrayAuthor.getJSONObject(i);
                        SearchHandlerItem o = new SearchHandlerItem(
                                obj.getString("Title"),
                                obj.getString("AuthorName"),
                                obj.getString("BookId")
                        );

                        list.add(o);


                    }
                }

                Log.d("ListSize", toString().valueOf(list.size()));
                adapter =new SearchHandlerAdapter(list,getApplicationContext());
                recyclerViewSearchHandler.setAdapter(adapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
