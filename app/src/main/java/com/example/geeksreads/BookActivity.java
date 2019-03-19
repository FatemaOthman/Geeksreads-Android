package com.example.geeksreads;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BookActivity extends AppCompatActivity {

    ImageView BookCover;
    Context mContext;
    TextView BookTitle;
    TextView BookAuthor;
    TextView RatingsNumber;
    TextView ReviewsNumber;
    Spinner BookOptions;
    RatingBar BookRatings;
    TextView BookDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mContext = this;


        BookCover = findViewById(R.id.BookCover);
        BookTitle = findViewById(R.id.BookNameTxt);
        BookAuthor = findViewById(R.id.AuthorNameTxt);
        RatingsNumber = findViewById(R.id.RatingsNumberTxt);
        ReviewsNumber = findViewById(R.id.ReviewsNumberTxt);
        BookOptions = findViewById(R.id.OptionsDropList);
        BookRatings = findViewById(R.id.ratingBar);
        BookDescription = findViewById(R.id.DescriptionTxt);

        GetImage getCover = new GetImage();
        getCover.execute();
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
        return super.onCreateOptionsMenu(menu);
    }


    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL("http://xxx.xxx.xxx/image.jpg");
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
            BookCover.setImageBitmap(result);
        }
    }


    public class RequestTask extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 3000;
        public static final int CONNECTION_TIMEOUT = 3000;
        public Boolean internetConn = false;

        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                BookTitle.setText(jsonObject.getString("BookTitle"));
                BookAuthor.setText(jsonObject.getString("BookAuthor"));
                //TODO: Adding Others TextViews and Edit the Keys.

            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}

