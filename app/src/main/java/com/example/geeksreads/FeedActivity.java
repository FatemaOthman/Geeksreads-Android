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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geeksreads.views.LoadingView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xml.sax.ContentHandler;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static String sForTestFeeditemsCount;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<FeedModel> list;
    Context mContext;
    View rootView;
    LoadingView Loading;

    /* SideBar Views */
    ImageView userPhoto;
    TextView userName;
    TextView followersCount;
    TextView booksCount;
    MenuItem FollowItem;
    MenuItem BookItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* If user is already logged in, Skip and go to Main Activity */
        mContext = this;
        UserSessionManager.Initialize(mContext);
        UserSessionManager.UserSessionState currentUserState = UserSessionManager.getCurrentState();

        if (currentUserState == UserSessionManager.UserSessionState.USER_LOGGED_IN)
        {
            /* Stay Here */
        }
        else if (currentUserState == UserSessionManager.UserSessionState.USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN)
        {
            /* Go to Next Activity Layout */
            Intent myIntent = new Intent(FeedActivity.this, LoginActivity.class);
            myIntent.putExtra("FROM", "FEED");
            startActivity(myIntent);
        }
        else
        {
            /* Go to Next Activity Layout */
            Intent myIntent = new Intent(FeedActivity.this, SignupActivity.class);
            myIntent.putExtra("FROM", "FEED");
            startActivity(myIntent);

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Loading = new LoadingView(null, (FrameLayout)findViewById(R.id.progressBarHolder), (TextView)findViewById(R.id.ProgressName));
        Intent NotificationService = new Intent(this, NotificationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(NotificationService);
        }
        startService(NotificationService);

        recyclerView = (RecyclerView) findViewById(R.id.FeedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rootView=findViewById(R.id.toolbar);
        mContext = this;

        /* ToolBar and SideBar Setups */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
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
                Intent mIntent = new Intent(FeedActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(FeedActivity.this, Profile.class);
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
        String UrlShelvesDetails = APIs.API_GET_SHELVES_COUNT;
        getShelvesDetails.execute(UrlShelvesDetails,UserSessionManager.getUserToken().toString());


        list = new ArrayList<>();
        Log.d("Token",UserSessionManager.getUserToken());
        String Url= APIs.API_USER_STATUS;
        GetFeedDetails getFeedDetails= new GetFeedDetails(UserSessionManager.getUserToken());
        getFeedDetails.execute(Url);


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
           /* String fromActivity = getIntent().getStringExtra("FROM");
            if ((fromActivity.equals("SIGNUP") || fromActivity.equals("SIGNIN")))
            {
                this.moveTaskToBack(true);
            }
            else*/
            {
                super.onBackPressed();
            }
        }
    }

    /**
     * @param menu : Menu object in the toolbar
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
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(800);
        searchView.setQueryHint("Search books");
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                rootView.requestFocus();
                Intent intent = new Intent(FeedActivity.this,SearchHandlerActivity.class);
                intent.putExtra("CallingActivity","FeedActivity");
                startActivity(intent);

            }
        });


        MenuItem item1 = menu.findItem(R.id.NotificationButton);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mIntent = new Intent(FeedActivity.this, NotificationActivity.class);
                startActivity(mIntent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param menuItem : item in menu of the toolbar
     * @return boolean "true"
     * Overrided Function to create sidebar and decide what to on clicking on it's menu items.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.Followers) {
            Intent myIntent = new Intent(FeedActivity.this, FollowActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.MyBooks) {
            Intent myIntent = new Intent(FeedActivity.this, MyBooksShelvesActivity.class);
            startActivity(myIntent);
        }
        else if (id == R.id.Signout) {
            Intent myIntent = new Intent(FeedActivity.this, SignOutActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                FeedActivity.GetUserPicture Pic = new FeedActivity.GetUserPicture();
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

    @SuppressLint("StaticFieldLeak")
    private class GetFeedDetails extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
        String userToken;

        public GetFeedDetails(String userToken) {
            this.userToken = userToken;
        }

        @Override
        protected void onPreExecute() {
            Loading.Start("Loading...");
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
                newJson.put("UserId",UserSessionManager.getUserID());
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
                final JSONArray jsonArray = new JSONArray(result);
                sForTestFeeditemsCount=String.valueOf(jsonArray.length());
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject o = jsonArray.getJSONObject(i);
                    FeedModel B;
                    switch(o.getString("StatusType"))
                    { case "Review":
                        B=new FeedModel(
                            o.getString("ReviewDate"),
                            o.getString("ReviewMakerId"),
                           "",
                            o.getString("StatusType"),
                            o.getString("ReviewId"),
                            "",
                            o.getString("BookName"),
                            "Add to shelf",
                            o.getString("BookId"),
                            "",
                            o.getString("ReviewMakerName"),
                            o.getString("ReviewMakerPhoto"),
                           "",
                            o.getString("AuthorName"),
                            o.getString("AuthorId"),
                            o.getString("BookPhoto"),
                            o.getString("ReviewBody"),
                            o.getBoolean("ReviewIsLiked")

                    );
                        break;
                        case"Comment":

                      B =new FeedModel(
                            o.getString("CommentDate"),
                            o.getString("ReviewMakerId"),
                            o.getString("CommentMakerId"),
                            o.getString("StatusType"),
                            o.getString("ReviewId"),
                            o.getString("CommentId"),
                           "",
                           "",
                           "",
                            o.getString("CommentMakerName"),
                            o.getString("ReviewMakerName"),
                            o.getString("ReviewMakerPhoto"),
                            o.getString("CommentMakerPhoto"),
                            "",
                           "",
                            "",
                            o.getString("ReviewBody"),
                              true

                    );
                      break;
                        default:
                            B=new FeedModel("","","","","","","","","","","","","","","","","",true);
                    }



                    list.add(B);
                }
                adapter =new FeedAdapter((ArrayList)list,mContext);
                recyclerView.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("ErrorFeed",e.getMessage());
            }
            Loading.Stop();
        }

    }




}