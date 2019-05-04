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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


/**
 * Class to Adapt Notification ListView
 */
public class Notification_JSONAdapter extends BaseAdapter {

    private final Context context;
    private JSONArray data;
    private String[] bookID = new String[100];
    private String[] bookName = new String[100];
    private String[] type = new String[100];

    Notification_JSONAdapter(Context context, JSONArray data) {
        this.data = data;
        this.context = context;
    }

    public String getBookID(int position)
    {
        return bookID[position];
    }

    public String getBookName(int position)
    {
        return bookName[position];
    }

    public String getType(int position){
        return type[position];
    }

    /**
     * @return Count of items in List
     */
    @Override
    public int getCount() {
        return data.length();
    }

    /**
     * @param i : Item Position
     * @return The item in this position
     */
    @Override
    public Object getItem(int i) {

        try {
            return data.getJSONObject(i);
        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        return null;
    }

    /**
     * @param position : position of item i needed.
     * @return the id of this item.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }


    /**
     * @param i : Position of the view in list.
     * @param view : The view I'll return to display.
     * @param viewGroup : All the views.
     * @return The final view to be displayed.
     */
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = Objects.requireNonNull(inflater).inflate(R.layout.notification_template, viewGroup, false);

        FixImagePosition holder = new FixImagePosition();
        holder.position = i;
        holder.Cover = itemView.findViewById(R.id.imageView);

        try {

            String notificationType = data.getJSONObject(i).getString("NotificationType");
            type[i] = notificationType;
            String notificationBody , NotificationDate;
            if (notificationType.equals("ReviewLike"))
            {
                notificationBody = data.getJSONObject(i).getString("MakerName") + " Liked your review on "
                                 + data.getJSONObject(i).getString("BookName");

                NotificationDate = data.getJSONObject(i).getString("ReviewDate");
                bookID[i] = data.getJSONObject(i).getString("BookId");
                bookName[i] = data.getJSONObject(i).getString("BookName");
            }
            else if (notificationType.equals("Comment"))
            {
                notificationBody = data.getJSONObject(i).getString("MakerName") + " commented on review in "
                        + data.getJSONObject(i).getString("BookName");

                NotificationDate = data.getJSONObject(i).getString("CommentDate");
                bookID[i] = data.getJSONObject(i).getString("BookId");
                bookName[i] = data.getJSONObject(i).getString("BookName");
            }
             else
            {
                notificationBody = data.getJSONObject(i).getString("MakerName") + " Started Following You";
                NotificationDate = data.getJSONObject(i).getString("FollowDate");
                bookID[i] = data.getJSONObject(i).getString("MakerId");
            }
            TextView notificationContent = itemView.findViewById(R.id.NotificationContent);
            notificationContent.setText(notificationBody);

            TextView notificationDate = itemView.findViewById(R.id.NotificationDate);
            notificationDate.setText(NotificationDate);


            GetImage getCover = new GetImage(i,holder);
            getCover.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,data.getJSONObject(i).getString("MakerPhoto"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemView;
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
