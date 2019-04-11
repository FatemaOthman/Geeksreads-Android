package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class BookList_JSONAdapter extends BaseAdapter {

    private final Context context;
    private JSONArray data;
    private ArrayList<Bitmap> Covers ;

    public BookList_JSONAdapter(Context context, JSONArray data) {
        this.data = data;
        this.context = context;
        Covers = new ArrayList<>();
    }

    public int getCount() {
        return data.length();
    }

    public Object getItem(int i) {

        try {
            return data.getJSONObject(i);
        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        return null;
    }

    public long getItemId(int i) {

        try {
            JSONObject object = data.getJSONObject(i);
            return object.getLong("id");
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        return -1;
    }

    // returns the view of a single row

    @SuppressLint("ViewHolder")
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = Objects.requireNonNull(inflater).inflate(R.layout.book_template, viewGroup, false);
        try {
            TextView BookName = itemView.findViewById(R.id.BookNameTxt);
            BookName.setText(data.getJSONObject(i).getString("Title"));


            TextView AuthorName = itemView.findViewById(R.id.ByAuthorNameTxt);
            AuthorName.setText(String.format("By: %s", data.getJSONObject(i).getString("Author")));

            TextView RatingNumber = itemView.findViewById(R.id.BookRatingsTxt);
            RatingNumber.setText(data.getJSONObject(i).getString("BookRating"));

            TextView Pages = itemView.findViewById(R.id.pageNumbers);
            Pages.setText(String.format("%s pages.", data.getJSONObject(i).getString("Pages")));

            TextView BookData = itemView.findViewById(R.id.PublishData);
            BookData.setText(String.format("Published on %s, By: %s", data.getJSONObject(i).getString("Published"),data.getJSONObject(i).getString("Publisher")));

            ImageView BookCover = itemView.findViewById(R.id.BookImage);
            //BookCover.getDrawable(data.getJSONObject(i).getString("value"))


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemView;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
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
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                // Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //mProgressBar.setVisibility(View.GONE);
            //sForTestBookActivity = "Done";
            //bookCover.setImageBitmap(result);
            Covers.add(result);

        }
    }
}
