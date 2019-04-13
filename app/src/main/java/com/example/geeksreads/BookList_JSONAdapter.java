package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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
    ImageView BookCover;

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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = Objects.requireNonNull(inflater).inflate(R.layout.book_template, viewGroup, false);
        try {

            FixImagePosition holder = new FixImagePosition();
            holder.position = i;
            holder.Cover = view.findViewById(R.id.BookImage);

            TextView BookName = view.findViewById(R.id.BookNameTxt);
            BookName.setText(data.getJSONObject(i).getString("Title"));

            TextView AuthorName = view.findViewById(R.id.ByAuthorNameTxt);
            AuthorName.setText(String.format("By: %s", data.getJSONObject(i).getString("Author")));

            TextView RatingNumber = view.findViewById(R.id.BookRatingsTxt);
            RatingNumber.setText(data.getJSONObject(i).getString("BookRating"));

            TextView Pages = view.findViewById(R.id.pageNumbers);
            Pages.setText(String.format("%s pages.", data.getJSONObject(i).getString("Pages")));

            TextView BookData = view.findViewById(R.id.PublishData);
            BookData.setText(String.format("Published on %s, By: %s", data.getJSONObject(i).getString("Published"),data.getJSONObject(i).getString("Publisher")));

            BookCover = view.findViewById(R.id.BookImage);
            GetImage getCover = new GetImage(i,holder);
            //getCover.execute(data.getJSONObject(i).getString("Cover"));
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
    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private FixImagePosition mBookCover;

        public GetImage(int position, FixImagePosition holder)
        {
            mPosition = position;
            mBookCover = holder;
        }
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
            Covers.add(result);
            if (mBookCover.position == mPosition) {
                mBookCover.Cover.setImageBitmap(result);
            }
            Log.d("Shrouk", Integer.toString(Covers.size()));

        }
    }

    private static class FixImagePosition {
        public ImageView Cover;
        public int position;
    }
}
