package com.example.geeksreads;


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
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class BookActivity extends AppCompatActivity {

    ImageView BookCover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        BookCover = findViewById(R.id.BookCover);
        TextView BookTitle = findViewById(R.id.BookNameTxt);
        TextView BookAuthor = findViewById(R.id.AuthorNameTxt);
        TextView RatingsNumber = findViewById(R.id.RatingsNumberTxt);
        TextView ReviewsNumber = findViewById(R.id.ReviewsNumberTxt);
        Spinner BookOptions = findViewById(R.id.OptionsDropList);
        RatingBar BookRatings = findViewById(R.id.ratingBar);
        TextView BookDescription = findViewById(R.id.DescriptionTxt);

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

    //TODO: Adding JSON Async Task to get All Books information.

}

