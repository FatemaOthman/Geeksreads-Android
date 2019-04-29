package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Class extends BaseAdapter to adapt the list view.
 */
public class BookList_JSONAdapter extends BaseAdapter {

    private final Context context;
    private JSONArray data;
    private String bookID;

    public BookList_JSONAdapter(Context context, JSONArray data) {
        this.data = data;
        this.context = context;
    }

    /**
     * @return Number of items in the list.
     */
    public int getCount() {
        return data.length();
    }

    /**
     * @return Book ISBN to get the book itself on clicking.
     */
    public String getBookID()
    {
        return bookID;
    }

    /**
     * @param i : Position of the item in list.
     * @return The Object in this position.
     */
    public Object getItem(int i) {

        try {
            return data.getJSONObject(i);
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        return null;
    }

    /**
     * @param i : Position of item in list
     * @return The ID of this item.
     */
    public long getItemId(int i) {

        try {
            JSONObject object = data.getJSONObject(i);
            object.get("ID");
            return i;
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        return -1;
    }

    /**
     * @param i : Position of the view in list.
     * @param view : The view I'll return to display.
     * @param viewGroup : All the views.
     * @return The final view to be displayed.
     */
    @SuppressLint("ViewHolder")
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = Objects.requireNonNull(inflater).inflate(R.layout.book_template, viewGroup, false);
        try {

            FixImagePosition holder = new FixImagePosition();
            holder.position = i;
            holder.Cover = view.findViewById(R.id.BookImage);

            bookID = data.getJSONObject(i).getString("ID");

            TextView BookName = view.findViewById(R.id.BookNameTxt);
            BookName.setText(data.getJSONObject(i).getString("Title"));

            TextView AuthorName = view.findViewById(R.id.ByAuthorNameTxt);
            AuthorName.setText(String.format("By: %s", data.getJSONObject(i).getString("Author")));

            String Ratings = data.getJSONObject(i).getString("BookRating");
            TextView RatingNumber = view.findViewById(R.id.ratingBar);
            RatingNumber.setText(String.format("%s", Ratings));

            TextView RatingCount = view.findViewById(R.id.BookRatingsTxt);
            RatingCount.setText(String.format("  %s Ratings", data.getJSONObject(i).getString("ratingcount")));

            RatingBar bookStars = view.findViewById(R.id.bookRatingStars);
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

            TextView Pages = view.findViewById(R.id.pageNumbers);
            Pages.setText(String.format("%s pages.", data.getJSONObject(i).getString("Pages")));

            TextView BookData = view.findViewById(R.id.PublishData);
            BookData.setText(String.format("Published on %s, By: %s", data.getJSONObject(i).getString("Published"),data.getJSONObject(i).getString("Publisher")));

            GetImage getCover = new GetImage(i,holder);
            getCover.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,data.getJSONObject(i).getString("Cover"));

         } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private FixImagePosition mBookCover;

        public GetImage(int position, FixImagePosition holder)
        {
            mPosition = position;
            mBookCover = holder;
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
            if (mBookCover.position == mPosition) {
                mBookCover.Cover.setImageBitmap(result);
            }
        }
    }

    /**
     * A Class to fix the image position in list.
     * The AsyncTask is slow comparing with the list fetching.
     */
    private static class FixImagePosition {
        public ImageView Cover;
        public int position;
    }
}
