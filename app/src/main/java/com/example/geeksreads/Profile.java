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
import android.util.Log;
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

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static String ForTestProfilePicture, ForTestFollowersCount, ForTestFollowingCount;
    public static ImageView UserPhoto;
    Context mContext;
    TextView FollowersCount;
    TextView FollowingCount;
    TextView BooksCount;
    Button EditProfileButton;

    /* SideBar Views */
    ImageView userPhoto;
    TextView userName;
    TextView followersCount;
    TextView booksCount;
    MenuItem FollowItem;
    MenuItem BookItem;

    /*
    public static String getCurrentID() {
        return CurrentUser;
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mContext = this;

        UserPhoto = findViewById(R.id.UserProfilePhoto);
        BooksCount = findViewById(R.id.NumberOfBooks);
        FollowersCount = findViewById(R.id.ActualFollowersCount);
        FollowingCount = findViewById(R.id.ActualFollowingCount);
        EditProfileButton = findViewById(R.id.edit_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        JSONObject JSON = new JSONObject();
        String UrlSideBar = "http://geeksreads.000webhostapp.com/Fatema/SideBar.php";
        /*
        GetSideBarDetails getSideBarDetails = new GetSideBarDetails();
        getSideBarDetails.execute(UrlSideBar, JSON.toString());
        */
        EditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Intent myIntent = new Intent(Profile.this,FollowActivity.class);
                Intent myIntent = new Intent(Profile.this, EditProfileActivity.class);
                myIntent.putExtra("UserID", UserSessionManager.getUserID());
                startActivity(myIntent);
            }
        });


        FollowersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Intent myIntent = new Intent(Profile.this,FollowActivity.class);
                Intent myIntent = new Intent(Profile.this, FollowActivity.class);
                myIntent.putExtra("UserID", UserSessionManager.getUserID());
                startActivity(myIntent);
            }
        });

        FollowingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this, FollowActivity.class);
                myIntent.putExtra("UserID", UserSessionManager.getUserID());
                startActivity(myIntent);
            }
        });


        //In my code here, I am sending the user ID to the backend:
        JSONObject mJSON = new JSONObject();

        try {

            mJSON.put("token", UserSessionManager.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Calling Async Task with my server url

        String UrlService = APIs.API_GET_USER_INFO;
        Profile.GetProfileDetails MyProfile = new Profile.GetProfileDetails();
        MyProfile.execute(UrlService, mJSON.toString());

        Profile.UpdateBookShelfCount updateReadShelf = new Profile.UpdateBookShelfCount(UserSessionManager.getUserToken());

        /* URL For Get Shelves Count API */
        String GetShelvesCountUrl = APIs.API_GET_SHELVES_COUNT;

        updateReadShelf.execute(GetShelvesCountUrl);


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
     * @param menu : Menu object in the toolbar
     * @return super.onCreateOptionsMenu(menu)
     *  Overrided Function to create the toolbar and decide what to do when click it's menu items.
     */
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
                Intent mIntent = new Intent(Profile.this, NotificationActivity.class);
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

        if (id == R.id.Home) {

            Intent myIntent = new Intent(Profile.this, FeedActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.Followers) {
            Intent myIntent = new Intent(Profile.this, FollowActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.MyBooks) {
            Intent myIntent = new Intent(Profile.this, MyBooksShelvesActivity.class);
            startActivity(myIntent);
        }
        else if (id == R.id.Signout) {
            Intent myIntent = new Intent(Profile.this, SignOutActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*  Class GetUserPicture:
     *      Gets user picture from the url and changes it into bitmap
     *      sets the imageView into the same bitmap
     */
    @SuppressLint("StaticFieldLeak")
    private class GetUserPicture extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String photoUrl = params[0];
                URL url = new URL(photoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDefaultUseCaches(false);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            UserPhoto.setImageBitmap(result);

        }
    }


    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    @SuppressLint("StaticFieldLeak")
    public class GetProfileDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        /**
         * doInBackground: Returns result string through sending and HTTP request and receiving the response.
         *
         * @param params
         * @return result
         */

        //////////////////////////////////////////////////////////////////////////////////////////
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

                switch (String.valueOf(http.getResponseCode())) {
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
                        Log.d("Test","String.valueOf(http.getResponseCode()): "+ String.valueOf(http.getResponseCode()));
                        result = "{\"ReturnMsg\":\"An Error Occurred!\"}";
                        break;
                }

                http.disconnect();
                Log.d("AMR", result);
                return result;

            }
            /* Handling Exceptions */ catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }


        /////////////////////////////////////////////////////////////////////////////////////////
        /**
         * onPostExecute: Takes the string result and treates it as a json object
         * to set data of:
         *  -Followers Count
         *  -Following Count
         *  -Books Count
         *  -Profile Picture
         *
         * @param result
         */
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                FollowersCount.setText(jsonObject.getString("NoOfFollowers"));
                FollowingCount.setText(jsonObject.getString("NoOfFollowings"));
                Profile.GetUserPicture Pic = new Profile.GetUserPicture();
                Pic.execute(jsonObject.getString("photo"));
                ForTestProfilePicture = jsonObject.getString("photo");
                ForTestFollowersCount = FollowersCount.getText().toString();
                ForTestFollowingCount = FollowingCount.getText().toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Class that get sidebar profile pic. from server
     */
    @SuppressLint("StaticFieldLeak")
    private class GetPicture extends AsyncTask<String, Void, Bitmap> {

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
                switch (String.valueOf(http.getResponseCode())) {
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

                JSONObject jsonObject = new JSONObject(result);
                FollowItem.setTitle("Followers   " + jsonObject.getString("Followers"));
                BookItem.setTitle("My Books   " + jsonObject.getString("CountBooks"));
                userName.setText(jsonObject.getString("UserName"));
                GetPicture Pic = new GetPicture();
                Pic.execute(jsonObject.getString("photourl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    ///////////////////////////////////////


    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    @SuppressLint("StaticFieldLeak")
    public class UpdateBookShelfCount extends AsyncTask<String, String, String> {
        static final String REQUEST_METHOD = "POST";
        String userToken;

        UpdateBookShelfCount(String userToken) {
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
                switch (String.valueOf(http.getResponseCode())) {
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
                String FullCount = Integer.toString(Integer.parseInt(readCount) + Integer.parseInt(wantToReadCount) + Integer.parseInt(readingCount));
                BooksCount.setText(FullCount + " " + "Books");

            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error in loading shelves data", Toast.LENGTH_SHORT).show();
            }
        }

    }


    /////////////////////////////////////
}
