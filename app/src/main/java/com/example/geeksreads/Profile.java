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
    View rootView;
    Button readButton;
    Button currentlyReadingButton;
    Button wantToReadButton;

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
        rootView=findViewById(R.id.toolbar);
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

        final String urlService = APIs.API_GET_SHELVES_COUNT;
        UpdateBookShelf updateShelf = new UpdateBookShelf(UserSessionManager.getUserToken());
        updateShelf.execute(urlService);
        /////////////////////////////////////////////////////
        readButton = findViewById(R.id.ReadBtn);
        currentlyReadingButton = findViewById(R.id.CurrentlyReadingBtn);
        wantToReadButton = findViewById(R.id.WantToReadBtn);

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(Profile.this, ReadBooksActivity.class);
                startActivity(myIntent);

            }
        });
        currentlyReadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Profile.this, CurrentlyReadingActivity.class);
                startActivity(myIntent);

            }
        });
        wantToReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Profile.this, WantToReadActivity.class);
                startActivity(myIntent);

            }
        });


        ///////////////////////////////////////////
        EditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this, EditProfileActivity.class);
                myIntent.putExtra("UserID", UserSessionManager.getUserID());
                startActivity(myIntent);
            }
        });


        FollowersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                rootView.requestFocus();
                Intent intent = new Intent(Profile.this,SearchHandlerActivity.class);
                intent.putExtra("CallingActivity","Profile");
                startActivity(intent);

            }
        });

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
                http.setRequestProperty("x-auth-token", UserSessionManager.getUserToken());

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
                Log.d("AMR", "Profile: " + result);
                JSONObject jsonObject = new JSONObject(result);
                FollowersCount.setText(jsonObject.getString("NoOfFollowers"));
                FollowingCount.setText(jsonObject.getString("NoOfFollowings"));
                Profile.GetUserPicture Pic = new Profile.GetUserPicture();
                Pic.execute(jsonObject.getString("Photo"));
                ForTestProfilePicture = jsonObject.getString("Photo");
                ForTestFollowersCount = FollowersCount.getText().toString();
                ForTestFollowingCount = FollowingCount.getText().toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    ///////////////////////////////////////

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    @SuppressLint("StaticFieldLeak")
    public class UpdateBookShelfCount extends AsyncTask<String, String, String> {
        static final String REQUEST_METHOD = "POST";
        String userToken;

        /**
         * Constructor for UpdateBookSHelfCountClass
         *
         * @param userToken Parameter to send user token
         */
        public UpdateBookShelfCount(String userToken) {
            this.userToken = userToken;
        }

        /**
         * Function to be done before Executing, it starts Loading Animation
         */
        @Override
        protected void onPreExecute() {
           /*
           Do Nothing
            */
        }

        /**
         * Function that executes the logic needed in the background thread
         */
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
                newJson.put("token", UserSessionManager.getUserToken());
                newJson.put("UserId", UserSessionManager.getUserID());
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
                Log.d("AMR", "BooksCount: " + FullCount);
            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error in loading shelves data", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * @author Mahmoud MORSY
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class UpdateBookShelf extends AsyncTask<String, String, String> {
        static final String REQUEST_METHOD = "POST";
        String userToken;

        /**
         * Constructor for UpdateBookSHelfCountClass
         *
         * @param userToken Parameter to send user token
         */
        public UpdateBookShelf(String userToken) {
            this.userToken = userToken;
        }

        /**
         * Function to be done before Executing, it starts Loading Animation
         */
        @Override
        protected void onPreExecute() {
        }

        /**
         * Function that executes the logic needed in the background thread
         */
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
                newJson.put("token", UserSessionManager.getUserToken());
                newJson.put("UserId", UserSessionManager.getUserID());
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

        /**
         * Function that does the needed actions in layout and finish loading animation
         */
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
                wantToReadBtn.setText("Want to Read  " + wantToReadCount);
                currentlyReadingBtn.setText("Currently Reading  " + readingCount);
            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error in loading shelves data", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /////////////////////////////////////
}
