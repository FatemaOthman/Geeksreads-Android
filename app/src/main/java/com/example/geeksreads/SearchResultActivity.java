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
import android.util.AtomicFile;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CustomFunctions.APIs;

public class SearchResultActivity extends AppCompatActivity {
    RecyclerView recyclerViewSearchHandler;
    RecyclerView.Adapter adapter;
    private List<BookItem> list;
    private static String CallingActivity;
    TextView moreSearchResults;
    Context mContext;
    public static int sFortTestNumOfResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        recyclerViewSearchHandler = findViewById(R.id.SearchResultRecycler);
        recyclerViewSearchHandler.setHasFixedSize(true);
        recyclerViewSearchHandler.setLayoutManager(new LinearLayoutManager(this));
        mContext = this;
        list=new ArrayList<>();
        CallingActivity=getIntent().getStringExtra("CallingActivity");

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String Text=getIntent().getStringExtra("queryText");

        String UrlSearch=APIs.API_SEARCH;
        SearchResultDetails searchResultDetails = new SearchResultDetails();
        searchResultDetails.execute(UrlSearch,Text);

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

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Intent intent = new Intent(SearchResultActivity.this,SearchHandlerActivity.class);

                startActivity(intent);

            }
        });
        MenuItem item1 = menu.findItem(R.id.NotificationButton);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mIntent = new Intent(SearchResultActivity.this, NotificationActivity.class);
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
                intent = new Intent(SearchResultActivity.this,
                        AuthorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "BookActivity":

                intent = new Intent(SearchResultActivity.this,
                        BookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "ChangePasswordActivity":
                intent = new Intent(SearchResultActivity.this,
                        ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "CurrentlyReadingActivity":
                intent = new Intent(SearchResultActivity.this,
                        CurrentlyReadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "EditProfileActivity":
                intent = new Intent(SearchResultActivity.this,
                        EditProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "FeedActivity":
                intent = new Intent(SearchResultActivity.this,
                        FeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case"MyBookShelvesActivity":
                intent = new Intent(SearchResultActivity.this,
                        MyBooksShelvesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "NotificationActivity":
                intent = new Intent(SearchResultActivity.this,
                        NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;
            case"OtherProfileActivity":
                intent = new Intent(SearchResultActivity.this,
                        OtherProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case"WantToReadActivity":
                intent = new Intent(SearchResultActivity.this,
                        WantToReadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case "Profile":
                intent = new Intent(SearchResultActivity.this,
                        Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case"ReadBooksActivity":
                intent = new Intent(SearchResultActivity.this,
                        ReadBooksActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            default:
                intent = new Intent(SearchResultActivity.this,
                        FeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);


        }

    }


    @SuppressLint("StaticFieldLeak")
    private class SearchResultDetails extends AsyncTask<String, Void, String> {
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
                JSONArray jsonArray=new JSONArray(result);
                JSONArray ArrayTitle = jsonArray.getJSONArray(0);

                for(int i = 0; i< ArrayTitle.length(); i++)
                {
                    JSONObject obj=ArrayTitle.getJSONObject(i);
                    BookItem o = new BookItem(
                            obj.getString("Title"),
                            obj.getString("AuthorName"),
                            obj.getString("BookRating"),
                            obj.getString("RateCount"),
                            obj.getString("Cover"),
                            obj.getString("ReadStatus"),
                            obj.getString("BookId")

                    );

                    list.add(o);


                }
                if(jsonArray.length()>1)
                {
                    JSONArray ArrayAuthor = jsonArray.getJSONArray(1);
                    for(int i=0;i<ArrayAuthor.length();i++)
                    {
                        JSONObject obj=ArrayAuthor.getJSONObject(i);
                        BookItem o = new BookItem(
                                obj.getString("Title"),
                                obj.getString("AuthorName"),
                                obj.getString("BookRating"),
                                obj.getString("RateCount"),
                                obj.getString("Cover"),
                                obj.getString("ReadStatus"),
                                obj.getString("BookId")

                        );
                        list.add(o);


                    }
                }
                sFortTestNumOfResult=jsonArray.length();
                Log.d("ListSize", toString().valueOf(list.size()));
                adapter =new BookAdapter(list,getApplicationContext());
                recyclerViewSearchHandler.setAdapter(adapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }





}
