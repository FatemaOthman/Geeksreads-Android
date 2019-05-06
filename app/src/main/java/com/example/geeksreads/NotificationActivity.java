package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

public class NotificationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;

    /* SideBar Views */
    ImageView userPhoto;
    TextView userName;
    TextView followersCount;
    TextView booksCount;
    MenuItem FollowItem;
    MenuItem BookItem;
    View rootView;

    /**
     * @param savedInstanceState
     * Overrided Function to decide what will appear after starting this Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext = this;

        /* ToolBar and SideBar Setups */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        rootView=findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Notifications");
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /* Getting All views by id from SideBar Layout */
        Menu menu = navigationView.getMenu();
        MenuItem itemFollower = menu.findItem(R.id.Followers);
        followersCount = (TextView) itemFollower.getActionView();
        FollowItem = menu.findItem(R.id.Followers);
        followersCount.setTextColor(getResources().getColor(R.color.white));
        MenuItem itemBook = menu.findItem(R.id.MyBooks);
        booksCount = (TextView) itemBook.getActionView();
        booksCount.setTextColor(getResources().getColor(R.color.white));
        BookItem = menu.findItem(R.id.MyBooks);
        /* Get Header Items */
        View mHeader = navigationView.getHeaderView(0);
        userName = mHeader.findViewById(R.id.UserNameTxt);
        userPhoto = mHeader.findViewById(R.id.UserPhoto);
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(NotificationActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(NotificationActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        JSONObject jsonUserDetails = new JSONObject();
        try {
            jsonUserDetails.put("token", UserSessionManager.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String UrlSideBar = APIs.API_GET_USER_INFO;//"http://geeksreads.000webhostapp.com/Fatema/SideBar.php";
        GetSideBarDetails getSideBarDetails = new GetSideBarDetails();
        getSideBarDetails.execute(UrlSideBar, jsonUserDetails.toString());
        GetShelvesDetails getShelvesDetails = new GetShelvesDetails(UserSessionManager.getUserToken());
        String UrlShelvesDetails = APIs.API_USER_SHELVES;
        getShelvesDetails.execute(UrlShelvesDetails,UserSessionManager.getUserToken());



        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", UserSessionManager.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String UrlService = APIs.API_GET_USER_NOTIFICATIONS;

        mSwipeRefreshLayout = findViewById(R.id.notificationSwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /**
             *  Overrided Function to decide what to do when refreshing the layout.
             */
            @Override
            public void onRefresh() {
                GetNotificationsList performBackgroundTask = new GetNotificationsList();
                performBackgroundTask.execute(UrlService, jsonObject.toString());
            }
        });

        GetNotificationsList performBackgroundTask = new GetNotificationsList();
        performBackgroundTask.execute(UrlService, jsonObject.toString());

    }

    /**
     * @param menu : Toolbar menu object.
     * @return super.onCreateOptionsMenu(menu)
     *  Overrided Function to create the toolbar and decide what to do when click it's menu items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        MenuItem item = menu.findItem(R.id.menunotification);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search books");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                rootView.requestFocus();
                Intent intent = new Intent(NotificationActivity.this,SearchHandlerActivity.class);

                intent.putExtra("CallingActivity","NotificationActivity");
                startActivity(intent);

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Overrided Function to decide what to do ok pressing "Back" key.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @param menuItem : Items of Toolbar menu.
     * @return boolean "true"
     * Overrided Function to create sidebar and decide what to on clicking on it's menu items.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.Home) {

            Intent myIntent = new Intent(NotificationActivity.this, FeedActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.Followers) {
            Intent myIntent = new Intent(NotificationActivity.this, FollowActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.MyBooks) {
            Intent myIntent = new Intent(NotificationActivity.this, MyBooksShelvesActivity.class);
            startActivity(myIntent);
        }
        else if (id == R.id.Signout) {
            Intent myIntent = new Intent(NotificationActivity.this, SignOutActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A Private class that extend Async Task to connect to server in background.
     * It get user Notifications lists.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetNotificationsList extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;
        boolean TaskSuccess = false;

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
                //Create a URL object holding our url
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
            mSwipeRefreshLayout.setRefreshing(false);
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                ListView notificationList = findViewById(R.id.NotificationList);
                final Notification_JSONAdapter notificationJsonAdapter = new Notification_JSONAdapter(mContext, new JSONArray(result));
                notificationList.setAdapter(notificationJsonAdapter);
                notificationList.deferNotifyDataSetChanged();
                notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (notificationJsonAdapter.getType(position).equals("Comment") || notificationJsonAdapter.getType(position).equals("ReviewLike"))
                        {
                            Intent intent = new Intent(NotificationActivity.this, Reviews.class);
                            intent.putExtra("BookID",notificationJsonAdapter.getBookID(position));
                            intent.putExtra("BookName",notificationJsonAdapter.getBookName(position));
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(NotificationActivity.this, OtherProfileActivity.class);
                            intent.putExtra("FollowId",notificationJsonAdapter.getBookID(position));
                            startActivity(intent);
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Class that get sidebar profile pic. from server
     */
    @SuppressLint("StaticFieldLeak")
    private class GetUserPicture extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String photoUrl = params[0];
                URL url = new URL(photoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                // Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            userPhoto.setImageBitmap(result);

        }
    }

    /**
     * Class that get sidebar Data from server
     */
    @SuppressLint("StaticFieldLeak")
    private class GetSideBarDetails extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
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
                writer.write(JSONString);
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
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    default:
                        result = "{\"ReturnMsg\":\"An Error Occurred!\"}";
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

                JSONObject jsonObject = new JSONObject(result);
                FollowItem.setTitle("Followers   " + jsonObject.getString("NoOfFollowers"));
                // BookItem.setTitle("My Books   0" );
                userName.setText(jsonObject.getString("UserName"));
                GetUserPicture Pic = new GetUserPicture();
                Pic.execute(jsonObject.getString("Photo"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class GetShelvesDetails extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
        String userToken;

        public GetShelvesDetails(String userToken) {
            this.userToken = userToken;
        }

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
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
                JSONObject newJson = new JSONObject();
                newJson.put("token", userToken);
                writer.write(newJson.toString());
                writer.flush();
                writer.close();
                ops.close();

                /* A Stream object to get the returned data from API Call */
                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"Error Occurred.\"}";
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
            } catch (JSONException e) {
                e.printStackTrace();
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
                int TotalNumOfBooks = 0;
                TotalNumOfBooks+=jsonObject.getInt("NoOfRead")+jsonObject.getInt("NoOfReading")+jsonObject.getInt("NoOfWantToRead");


                BookItem.setTitle("My Books   "+TotalNumOfBooks);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
