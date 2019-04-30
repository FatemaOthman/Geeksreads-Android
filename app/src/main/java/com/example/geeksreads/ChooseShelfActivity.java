package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.UserSessionManager;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ChooseShelfActivity extends AppCompatActivity {

    ImageView bookImage;
    Context mContext;
    String shelfID;

    /**
     * @param savedInstanceState
     * Overrided Function to decide what will appear after starting this Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shelf);
        mContext = this;

        /* ToolBar Initializations */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add to my shelves");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Get the values from BookActivity */
        Intent intent = getIntent();
        String bookAuthorPassed = intent.getStringExtra("Author");
        String bookTitlePassed = intent.getStringExtra("Title");
        String bookRatingPassed = intent.getStringExtra("Rating");
        String bookRatingNumberPassed = intent.getStringExtra("RatingNumber");
        String pageNumbersPassed = intent.getStringExtra("Pages");
        String publishedDatePassed = intent.getStringExtra("published");
        String bookCoverURL = intent.getStringExtra("cover");
        final String BookStatus = intent.getStringExtra("BookStatus");
        final String getBookID = intent.getStringExtra("bookID");

        /* Get views from layout by IDs */
        TextView BookName = findViewById(R.id.BookNameTxt);
        TextView AuthorName = findViewById(R.id.ByAuthorNameTxt);
        TextView RatingNumber = findViewById(R.id.BookRatingsTxt);
        TextView Rating = findViewById(R.id.ratingBar);
        TextView Pages = findViewById(R.id.pageNumbers);
        TextView BookData = findViewById(R.id.PublishData);
        Button addShelf = findViewById(R.id.AddShelfBtn);
        RatingBar bookStars = findViewById(R.id.bookRatingStars);
        final RadioGroup ShelfChoosed= findViewById(R.id.radioGroup);
        final RadioButton readRadio = findViewById(R.id.radioRead);
        final RadioButton readingRadio = findViewById(R.id.radioReading);
        final RadioButton wantRadio = findViewById(R.id.radioWantRead);
        bookImage = findViewById(R.id.BookImage);
        bookImage.setBackgroundColor(getResources().getColor(R.color.colorToolbar));

        /* Set the values got from Book */
        BookName.setText(bookTitlePassed);
        AuthorName.setText(bookAuthorPassed);
        Rating.setText(bookRatingPassed);
        RatingNumber.setText(bookRatingNumberPassed);
        Pages.setText(pageNumbersPassed);
        BookData.setText(publishedDatePassed);
        bookStars.setRating(Float.parseFloat(bookRatingPassed));
        GetImage getCover = new GetImage();
        getCover.execute(bookCoverURL);

        /* Decide which radio group */
        if(BookStatus.equals("Read"))
        {
            readRadio.setChecked(true);
            readingRadio.setClickable(false);
            readingRadio.setTextColor(Color.GRAY);
            wantRadio.setClickable(false);
            wantRadio.setTextColor(Color.GRAY);
            addShelf.setClickable(false);
        }
        else if(BookStatus.equals("Currently Reading"))
        {
            wantRadio.setClickable(false);
            wantRadio.setTextColor(Color.GRAY);
            readingRadio.setChecked(true);
        }
        else if(BookStatus.equals("Want To Read"))
        {
            wantRadio.setChecked(true);
            readRadio.setClickable(false);
            readRadio.setTextColor(Color.GRAY);
        }

        /* On Adding to shelf Button Listener */
        addShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShelfChoosed.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(mContext,"You need to choose Shelf", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if (readRadio.isChecked())
                    {
                        shelfID = "Read";
                    }
                    else if (readingRadio.isChecked())
                    {
                        shelfID = "Reading";
                    }
                    else if (wantRadio.isChecked())
                    {
                        shelfID = "WantToRead";
                    }

                    JSONObject ReviewObject = new JSONObject();
                    try {
                        ReviewObject.put("token", UserSessionManager.getUserToken());
                        ReviewObject.put("bookId", getBookID);
                        ReviewObject.put("ShelfType", shelfID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String UrlService = null;
                    if (BookStatus.equals("Want To Read") && shelfID.equals("Reading"))
                    {
                        UrlService = "https://geeksreads.herokuapp.com/api/users/UpdateWantToReading";
                    }
                   else if (BookStatus.equals("Currently Reading") && shelfID.equals("Read"))
                    {
                        UrlService = "https://geeksreads.herokuapp.com/api/users/UpdateReadingToRead";
                    }
                    else
                    {
                        UrlService = "https://geeksreads.herokuapp.com/api/users/AddToShelf";
                    }

                    AddShelfTask addShelfTask = new AddShelfTask();
                    addShelfTask.execute(UrlService, ReviewObject.toString());
                }
            }
        });
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetImage extends AsyncTask<String, Void, Bitmap> {

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
            bookImage.setImageBitmap(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AddShelfTask extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;
        boolean TaskSuccess = false;

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
                http.setRequestProperty("content-type", "application/json");

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));

                writer.write(JSONString);
                writer.flush();
                writer.close();
                ops.close();

                if (String.valueOf(http.getResponseCode()).equals("200"))
                {
                    /* when stream success */
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
                else /* When error take place */
                {
                    InputStream es = http.getErrorStream();
                    BufferedReader readers = new BufferedReader(new InputStreamReader(es, StandardCharsets.ISO_8859_1));
                    String lines;
                    while ((lines = readers.readLine()) != null) {
                        result += lines;
                    }
                    readers.close();
                    es.close();
                    TaskSuccess = false;
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
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                Toast.makeText(mContext, jsonObject.getString("ReturnMsg"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Toast.makeText(mContext, "Error happen during adding to shelf", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
