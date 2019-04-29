package com.example.geeksreads;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class BookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String sForTestBookActivity;
    /* BookActivity Views */
    ImageView bookCover;
    Context mContext;
    TextView bookTitle;
    TextView bookAuthor;
    TextView ratingsNumber;
    TextView reviewsNumber;
    Button bookOptions;
    TextView bookRatings;
    TextView bookDescription;
    TextView publishingDate;
    TextView pageNumber;
    TextView ISBN;
    RatingBar bookStars;
    ProgressBar mProgressBar;
    String ImageURL;

    /* SideBar Views */
    ImageView userPhoto;
    TextView userName;
    TextView followersCount;
    TextView booksCount;
    MenuItem FollowItem;
    MenuItem BookItem;


    /**
     * @param savedInstanceState
     * Overrided Function to decide what will appear after starting this Activity.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mContext = this;


        Intent intent = getIntent();
        String getISBN = intent.getStringExtra("BookISBN");

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
                Intent mIntent = new Intent(BookActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(BookActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        JSONObject JSON = new JSONObject();
        String UrlSideBar = "http://geeksreads.000webhostapp.com/Fatema/SideBar.php";
        GetSideBarDetails getSideBarDetails = new GetSideBarDetails();
        getSideBarDetails.execute(UrlSideBar, JSON.toString());


        /* Getting All views by id from Book Layout */
        mProgressBar = findViewById(R.id.progressBar);
        bookCover = findViewById(R.id.BookCover);
        bookTitle = findViewById(R.id.BookTitleTxt);
        bookAuthor = findViewById(R.id.AuthorNameTxt);
        ratingsNumber = findViewById(R.id.RatingsNumberTxt);
        reviewsNumber = findViewById(R.id.ReviewsNumberTxt);
        bookOptions = findViewById(R.id.OptionsDropList);
        bookRatings = findViewById(R.id.RatingBar);
        bookDescription = findViewById(R.id.DescriptionTxt);
        publishingDate = findViewById(R.id.PublishedOnTxt);
        ISBN = findViewById(R.id.ISBN);
        bookStars = findViewById(R.id.BookRatingStars);
        pageNumber = findViewById(R.id.pages);

        bookOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BookActivity.this, ChooseShelfActivity.class);
                myIntent.putExtra("Author",bookAuthor.getText());
                myIntent.putExtra("Title", bookTitle.getText());
                myIntent.putExtra("Rating",bookRatings.getText());
                myIntent.putExtra("RatingNumber",ratingsNumber.getText());
                myIntent.putExtra("Pages",pageNumber.getText());
                myIntent.putExtra("published",publishingDate.getText());
                myIntent.putExtra("cover", ImageURL);
                startActivity(myIntent);
            }
        });

        bookAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(BookActivity.this, AuthorActivity.class);
                startActivity(myIntent);
            }
        });


        /* Creating Json Object to be send */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ISBN", getISBN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Calling Async Task with my server url */
        String UrlService = "http://geeksreads.000webhostapp.com/Shrouk/BookDetails.php";
        mProgressBar.setVisibility(View.VISIBLE);
        GetBookDetails getBookDetails = new GetBookDetails();
        getBookDetails.execute(UrlService, jsonObject.toString());

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
                Intent mIntent = new Intent(BookActivity.this, NotificationActivity.class);
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

            Intent myIntent = new Intent(BookActivity.this, FeedActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.Followers) {
            Intent myIntent = new Intent(BookActivity.this, FollowActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.MyBooks) {
            Intent myIntent = new Intent(BookActivity.this, MyBooksShelvesActivity.class);
            startActivity(myIntent);
        }
        else if (id == R.id.Signout) {
            Intent myIntent = new Intent(BookActivity.this, SignOutActivity.class);
            startActivity(myIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
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
                // Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mProgressBar.setVisibility(View.GONE);
            sForTestBookActivity = "Done";
            bookCover.setImageBitmap(result);

        }
    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetBookDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                String line;
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
            mProgressBar.setVisibility(View.GONE);
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage("Done");
                //dialog.show();

                /* Get Json Object from server and preview results on Layout views */
                JSONObject jsonObject = new JSONObject(result);
                bookTitle.setText(jsonObject.getString("Title"));
                bookAuthor.setText("By: " + "" + jsonObject.getString("Author"));
                ratingsNumber.setText(jsonObject.getString("ratingcount") + " " + "Ratings");
                reviewsNumber.setText(jsonObject.getString("textreviewscount") + " " + "Reviews");
                String Ratings = jsonObject.getString("BookRating");
                bookRatings.setText(Ratings);
                bookDescription.setText(jsonObject.getString("Description"));
                pageNumber.setText(jsonObject.getString("Pages") + " pages");
                publishingDate.setText("Published On" + "  " + jsonObject.getString("Published")
                        + ", By: " + jsonObject.getString("Publisher"));
                ISBN.setText("ISBN: " + jsonObject.getString("ISBN"));
                bookStars.setRating(Float.parseFloat(Ratings));
                if (Float.parseFloat(Ratings) <= 1)
                {
                    LayerDrawable stars = (LayerDrawable) bookStars.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if (Float.parseFloat(Ratings) <= 2) {
                    LayerDrawable stars = (LayerDrawable) bookStars.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.rgb(255,140,0), PorterDuff.Mode.SRC_ATOP);
                }
                else if (Float.parseFloat(Ratings) <= 3) {
                    LayerDrawable stars = (LayerDrawable) bookStars.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                }
                else if (Float.parseFloat(Ratings) <= 4) {
                    LayerDrawable stars = (LayerDrawable) bookStars.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                }
                else if (Float.parseFloat(Ratings) <= 5) {
                    LayerDrawable stars = (LayerDrawable) bookStars.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.rgb(34,139,34), PorterDuff.Mode.SRC_ATOP);
                }

                if (jsonObject.getString("ReadStatus").equals("Read"))
                {
                    bookOptions.setText("Read");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.ReadColor));
                }
                else if (jsonObject.getString("ReadStatus").equals("Want to Read"))
                {
                    bookOptions.setText("Want To Read");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.WantToReadColor));
                }
                else if (jsonObject.getString("ReadStatus").equals("Currently Reading"))
                {
                    bookOptions.setText("Currently Reading");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.ReadingColor));
                }
                else
                {
                    bookOptions.setText("Add to shelf");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.colorNotificationbar));
                }


                /* Start Async Task to get the image from url */
                GetImage getCover = new GetImage();
                ImageURL = jsonObject.getString("Cover");
                getCover.execute(ImageURL);

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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                String line;
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

