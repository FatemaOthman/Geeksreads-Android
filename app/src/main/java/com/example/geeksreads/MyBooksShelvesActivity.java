package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import CustomFunctions.APIs;

public class MyBooksShelvesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest_Read;
    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest_CurrentlyReading;
    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest_WantToRead;
    /* SideBar Views */
    ImageView userPhoto;
    TextView userName;
    TextView followersCount;
    TextView booksCount;
    MenuItem FollowItem;
    MenuItem BookItem;
    /**
     * Global Variables to Store Context of this Activity itself
     */
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books_shelves);
        mContext = this;

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
                Intent mIntent = new Intent(MyBooksShelvesActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MyBooksShelvesActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        JSONObject JSON = new JSONObject();
        String UrlSideBar = "http://geeksreads.000webhostapp.com/Fatema/SideBar.php";
        GetSideBarDetails getSideBarDetails = new GetSideBarDetails();
        getSideBarDetails.execute(UrlSideBar, JSON.toString());

        Button readButton = findViewById(R.id.ReadBtn);
        Button currentlyReadingButton = findViewById(R.id.CurrentlyReadingBtn);
        Button wantToReadButton = findViewById(R.id.WantToReadBtn);


        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MyBooksShelvesActivity.this, ReadBooksActivity.class);
                startActivity(myIntent);

            }
        });
        currentlyReadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MyBooksShelvesActivity.this, CurrentlyReadingActivity.class);
                startActivity(myIntent);

            }
        });
        wantToReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MyBooksShelvesActivity.this, WantToReadActivity.class);
                startActivity(myIntent);

            }
        });

        UpdateBookShelfCount updateReadShelf = new UpdateBookShelfCount(UserSessionManager.getUserToken());

        /* URL For Get Shelves Count API */
        String urlService = APIs.API_GET_SHELVES_COUNT;

        updateReadShelf.execute(urlService);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(800);
        searchView.setQueryHint("Search books");
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        MenuItem item1 = menu.findItem(R.id.NotificationButton);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mIntent = new Intent(MyBooksShelvesActivity.this, NotificationActivity.class);
                startActivity(mIntent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.Home) {

            Intent myIntent = new Intent(MyBooksShelvesActivity.this, FeedActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.Followers) {
            Intent myIntent = new Intent(MyBooksShelvesActivity.this, FollowActivity.class);
            startActivity(myIntent);
        }
        else if (id == R.id.Signout) {
            Intent myIntent = new Intent(MyBooksShelvesActivity.this, SignOutActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class UpdateBookShelfCount extends AsyncTask<String, String, String> {
        static final String REQUEST_METHOD = "POST";
        String userToken;

        public UpdateBookShelfCount(String userToken) {
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
                String readCount = jsonObject.getString("NoOfRead");
                String wantToReadCount = jsonObject.getString("NoOfWantToRead");
                String readingCount = jsonObject.getString("NoOfReading");

                Button readBtn = findViewById(R.id.ReadBtn);
                Button wantToReadBtn = findViewById(R.id.WantToReadBtn);
                Button currentlyReadingBtn = findViewById(R.id.CurrentlyReadingBtn);

                readBtn.setText("Read  " + readCount);
                sForTest_Read = "Read  " + readCount;
                wantToReadBtn.setText("Want to Read  " + wantToReadCount);
                sForTest_WantToRead = "Want to Read  " + wantToReadCount;
                currentlyReadingBtn.setText("Currently Reading  " + readingCount);
                sForTest_CurrentlyReading = "Currently Reading  " + readingCount;
            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error in loading shelves data", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Class that get sidebar profile pic. from server
     */
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
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
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
    private class GetSideBarDetails extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "GET";
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
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(JSONString, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                reader.close();
                ips.close();
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
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                FollowItem.setTitle("Followers   " + jsonObject.getString("Followers"));
                BookItem.setTitle("My Books   " + jsonObject.getString("CountBooks"));
                userName.setText(jsonObject.getString("UserName"));
                GetUserPicture Pic = new GetUserPicture();
                Pic.execute(jsonObject.getString("photourl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
