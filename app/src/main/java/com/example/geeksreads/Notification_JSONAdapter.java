package com.example.geeksreads;

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

public class Notification_JSONAdapter extends BaseAdapter {

    private JSONArray data;
    private final Context context;
    ImageView notificationImage;

    public  Notification_JSONAdapter(Context context,JSONArray data){
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int i) {

        try{
            return data.getJSONObject(i);
        }catch(JSONException jse){
            jse.printStackTrace();
        }

        return null;
    }

    @Override
    public long getItemId(int i) {

        try{
            JSONObject object = data.getJSONObject(i);
            return object.getLong("id");
        }catch(JSONException jse){
            jse.printStackTrace();
        }
        return -1;
    }

    // returns the view of a single row

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inflater.inflate(R.layout.book_template, viewGroup,false);
        try {
            TextView notificationContent = itemView.findViewById(R.id.NotificationContent);
            notificationContent.setText(data.getJSONObject(i).getString("bookname"));

            TextView notificationDate = itemView.findViewById(R.id.NotificationDate);
            notificationDate.setText(data.getJSONObject(i).getString("value"));

            TextView notificationTime = itemView.findViewById(R.id.NotificationTime);
            notificationTime.setText(data.getJSONObject(i).getString("value"));


            notificationImage = itemView.findViewById(R.id.NotificationImage);
            GetNotificationImage getNotificationImage = new GetNotificationImage();
            getNotificationImage.execute(data.getJSONObject(i).getString("value"));


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return itemView;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     *  The only Parameter is the Url.
     */
    private class GetNotificationImage extends AsyncTask<String, Void, Bitmap> {

        protected void onPreExecute() {
            //progress.setVisibility(View.VISIBLE);
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

            notificationImage.setImageBitmap(result);

        }
    }
}
