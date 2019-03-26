package com.example.geeksreads;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
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


public class BookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static String sForTestAuthor, sForTestTitle, sForTestRate, sForTestDate, sForTestBookActivity;
    ImageView bookCover;
    Context mContext;
    TextView bookTitle;
    TextView bookAuthor;
    TextView ratingsNumber;
    TextView reviewsNumber;
    Spinner bookOptions;
    TextView bookRatings;
    TextView bookDescription;
    TextView publishingDate;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mContext = this;

        /** Getting All views by id from Layout */
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


        /** Creating Json Object to be send */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", "value");
        }catch (JSONException e) {
            e.printStackTrace();
        }

        /** Calling Async Task with my server url */
        String UrlService = "http://geeksreads.000webhostapp.com/Shrouk/BookDetails.php";
        mProgressBar.setVisibility(View.VISIBLE);
        GetBookDetails getBookDetails = new GetBookDetails();
        getBookDetails.execute(UrlService,jsonObject.toString());


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        searchView.setMaxWidth(780);
        searchView.setQueryHint("Search books");
        searchView.setBackgroundColor(getResources().getColor(R.color.white));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    /**
     * Class that get image from Url and Add it to ImageView.
     *  The only Parameter is the Url.
     */
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
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
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
     *  The Parameters are host Url and toSend Data.
     */
    public class GetBookDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
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
        protected void onPostExecute(String result){
            mProgressBar.setVisibility(View.GONE);
            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage("Done");
                //dialog.show();

                /** Get Json Object from server and preview results on Layout views */
                JSONObject jsonObject = new JSONObject(result);
                bookTitle.setText(jsonObject.getString("Title"));
                bookAuthor.setText("By: " + "" +jsonObject.getString("Author"));
                ratingsNumber.setText(jsonObject.getString("ratingcount") + " " + "Ratings");
                reviewsNumber.setText(jsonObject.getString("textreviewscount")+ " " + "Reviews");
                bookRatings.setText(jsonObject.getString("averagerating"));
                bookDescription.setText(jsonObject.getString("bookdescription"));
                publishingDate.setText("Originally Published" + "  " + jsonObject.getString("originalpublicationday")
                                       + " - " + jsonObject.getString("originalpublicationmonth")
                                       + " - " + jsonObject.getString("originalpublicationyear"));

                /** Start Async Task to get the image from url */
                GetImage getCover = new GetImage();
                getCover.execute(jsonObject.getString("photourl"));

                sForTestAuthor = bookAuthor.getText().toString();
                sForTestTitle = bookTitle.getText().toString();
                sForTestRate = bookRatings.getText().toString();
                sForTestDate= publishingDate.getText().toString();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

}

