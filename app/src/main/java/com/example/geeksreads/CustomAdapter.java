package com.example.geeksreads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<UserDataModel> implements View.OnClickListener {

    Context mContext;
    ViewHolder viewHolder; // view lookup cache stored in tag
    private ArrayList<UserDataModel> dataSet;
    private int lastPosition = -1;

    public CustomAdapter(ArrayList<UserDataModel> data, Context context) {
        super(context, R.layout.single_follow, data);

        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {


        int position = (Integer) v.getTag();
        UserDataModel dataModel = getItem(position);

        Intent myIntent = new Intent(getContext(), OtherProfileActivity.class);
        myIntent.putExtra("UserID", dataModel.getID());
        mContext.startActivity(myIntent);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_follow, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.PicInfo = convertView.findViewById(R.id.item_Pic);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtName.setText(dataModel.getName());
        CustomAdapter.GetUserPicture Pic = new CustomAdapter.GetUserPicture();
        Pic.execute(dataModel.getPicLink());
        //  Log.i("AMR","IMAGE: "+dataModel.getPicLink());
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView PicInfo;
    }

    private class GetUserPicture extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String photoUrl = params[0];
                // Log.i("AMR","PICTURE: "+photoUrl);
                URL url = new URL(photoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            viewHolder.PicInfo.setImageBitmap(result);
        }
    }


}
