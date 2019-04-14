package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add o my shelves");
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
        String pageNumbersPassed = intent.getStringExtra("Pages");
        String publishedDatePassed = intent.getStringExtra("published");
        String bookCoverURL = intent.getStringExtra("cover");

        /* Get views from layout by IDs */
        TextView BookName = findViewById(R.id.BookNameTxt);
        TextView AuthorName = findViewById(R.id.ByAuthorNameTxt);
        TextView RatingNumber = findViewById(R.id.BookRatingsTxt);
        TextView Pages = findViewById(R.id.pageNumbers);
        TextView BookData = findViewById(R.id.PublishData);
        Button addShelf = findViewById(R.id.AddShelfBtn);
        Button addReview = findViewById(R.id.AddReviewBtn);
        final RadioGroup ShelfChoosed= findViewById(R.id.radioGroup);
        final RadioButton readRadio = findViewById(R.id.radioRead);
        final RadioButton readingRadio = findViewById(R.id.radioReading);
        final RadioButton wantRadio = findViewById(R.id.radioWantRead);
        bookImage = findViewById(R.id.BookImage);
        bookImage.setBackgroundColor(getResources().getColor(R.color.colorToolbar));

        /* Set the values got from Book */
        BookName.setText(bookTitlePassed);
        AuthorName.setText(bookAuthorPassed);
        RatingNumber.setText(bookRatingPassed);
        Pages.setText(pageNumbersPassed);
        BookData.setText(publishedDatePassed);
        GetImage getCover = new GetImage();
        getCover.execute(bookCoverURL);

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
                        shelfID = "0";
                    }
                    else if (readingRadio.isChecked())
                    {
                        shelfID = "1";
                    }
                    else if (wantRadio.isChecked())
                    {
                        shelfID = "2";
                    }
                    Toast.makeText(mContext,"You choosed shelf id " + shelfID, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* on Add Review button click listener */
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Redirect to Add Reviews ACtivity", Toast.LENGTH_SHORT).show();
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
}
