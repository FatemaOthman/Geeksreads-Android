package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static String sForTestAuthorName, sForTestAuthorRate, sForTestAuthorNumOfRates,sForTestAuthorNumOfReviews, sForTestAuthorPicURL, sForTestNumOfBooks,sFortTestFollowStatus, sForTestAuthorDescription;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<BookItem> list;
    ImageView AuthorPhoto;
    Context mContext;
    TextView AuthorName;
    TextView NumOfBooks;
    TextView AuthorRating;
    RatingBar AuthorRatingBar;
    TextView AuthorDescription;
    TextView AuthorNumsOfRating;
    TextView AuthorNumsOfReviews;
    String ImageURL;
    Button followingState;
    static boolean Follow=true;

    /* SideBar Views */
    ImageView userPhoto;
    TextView userName;
    TextView followersCount;
    TextView booksCount;
    MenuItem FollowItem;
    MenuItem BookItem;
    String AuthorID;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContext = this;
        Intent intent = getIntent();
        AuthorID=intent.getStringExtra("AuthorID");
        AuthorID = "5c911452938ffea87b4672d7";//intent.getStringExtra("AuthorID");


        /* ToolBar and SideBar Setups */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                Intent mIntent = new Intent(AuthorActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(AuthorActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        JSONObject JSON = new JSONObject();
        String UrlSideBar = "http://geeksreads.000webhostapp.com/Fatema/SideBar.php";
        GetSideBarDetails getSideBarDetails = new GetSideBarDetails();
        getSideBarDetails.execute(UrlSideBar, JSON.toString());


        /*Getting All views by id from AuthorActivity Layout*/
        AuthorName = findViewById(R.id.AuthorName);
        NumOfBooks = findViewById(R.id.NumberOfBooks);
        AuthorRating = findViewById(R.id.AuthorRating);
        AuthorRatingBar = findViewById(R.id.AuthorRatingBar);
        AuthorDescription = findViewById(R.id.AuthorDesc);
        AuthorPhoto = findViewById(R.id.AuthorPhoto);
        AuthorNumsOfRating=findViewById(R.id.NumsOfRatingAuthor);
        AuthorNumsOfReviews=findViewById(R.id.NumsOfReviewsAuthor);
        followingState=findViewById(R.id.Follow);

        /*Setting onClickListener for following button*/
        followingState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Follow= (authorFollowUnFollow(Follow)=="Following")?true:false;
               followingState.setText(authorFollowUnFollow(Follow));


            }
        });


        list = new ArrayList<>();
        JSONObject mJSON = new JSONObject();
       /*try {
            mJSON.put("auth_id", AuthorID);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        String UrlAuthor = "https://geeksreads.herokuapp.com/api/authors/id?auth_id=5c911452938ffea87b4672d7";


        GetAuthorDetails getAuthorDetails = new GetAuthorDetails();
        getAuthorDetails.execute(UrlAuthor, "5c911452938ffea87b4672d7");





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
                Intent mIntent = new Intent(AuthorActivity.this, NotificationActivity.class);
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

            Intent myIntent = new Intent(AuthorActivity.this, FeedActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.Followers) {
            Intent myIntent = new Intent(AuthorActivity.this, FollowActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.MyBooks) {
            Intent myIntent = new Intent(AuthorActivity.this, MyBooksShelvesActivity.class);
            startActivity(myIntent);
        }
        else if (id == R.id.Signout) {
            Intent myIntent = new Intent(AuthorActivity.this, SignOutActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
    * authorFollowUnFollow: gets called whenever follow/unfollow button is pressed, is supposed to change following status
    * @param Follow :boolean representing the following status

    * @return String representing the new following state after the click action is done
     */
    public static String authorFollowUnFollow( boolean Follow)
    {
        return Follow==false?"Following":"Follow";

    }



    @SuppressLint("StaticFieldLeak")
    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        protected void onPreExecute() {
            //mProgressBar.setVisibility(View.VISIBLE);
        }

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

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            AuthorPhoto.setImageBitmap(result);

        }
    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetAuthorDetails extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "GET";
        JSONObject mJSON = new JSONObject();

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String bookID = params[1];
            String result = "";

            // UrlString = UrlString + "?Json =" + params[1];
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
                final JSONObject jsonObject = new JSONObject(result);

                AuthorName.setText(jsonObject.getString("AuthorName"));
                // AuthorNumsOfRating.setText(jsonObject.getString("numofrates")+" ratings.");

                //  AuthorRating.setText(jsonObject.getString("rate"));
                //  AuthorRatingBar.setRating(Float.parseFloat((String)jsonObject.getString("rate")));

                AuthorDescription.setText(jsonObject.getString("About"));

                JSONArray array=jsonObject.getJSONArray("BookId");
                NumOfBooks.setText("Author of "+array.length()+" books.");
                // AuthorNumsOfReviews.setText(jsonObject.getString("numofreviews")+" reviews.");


                // if(/*jsonObject.getString("followed").equals("true")*/)
                // {
                followingState.setText("Following");
                Follow=true;
                // }
                /*else {
                    followingState.setText("Follow");
                    Follow=false;
                }*/

                /* Start Async Task to get the image from url */
                GetImage getAuthorPic = new GetImage();
                ImageURL = jsonObject.getString("Photo");
                getAuthorPic.execute(ImageURL);

                // sForTestAuthorName=(String)AuthorName.getText();
                // sForTestAuthorNumOfRates=(String) AuthorNumsOfRating.getText();
                // sForTestNumOfBooks=(String) NumOfBooks.getText();
                // sForTestAuthorDescription =(String) AuthorDescription.getText();
                // sForTestAuthorPicURL=ImageURL;
                // sFortTestFollowStatus=followingState.getText().toString();
                // sForTestAuthorRate=AuthorRating.getText().toString();
                // sForTestAuthorNumOfReviews=AuthorNumsOfReviews.getText().toString();
                //   JSONArray array=jsonObject.getJSONArray("authorbooks");
             /*   for(int i=0;i<array.length();i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    BookItem B =new BookItem(
                            o.getString("name"),
                            o.getString("author"),
                            o.getString("bookrate"),
                            o.getString("numofrates"),
                            o.getString("coverurl"),
                            o.getString("readingstatus"),
                            o.getString("BookId")
                    );
                    list.add(B);
                }
                adapter =new BookAdapter(list,getApplicationContext());
                recyclerView.setAdapter(adapter);*/

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
            String bookID = params[1];
            String result = "";

            // UrlString = UrlString + "?Json =" + params[1];
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
