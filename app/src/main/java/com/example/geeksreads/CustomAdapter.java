package com.example.geeksreads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<UserDataModel> implements View.OnClickListener {

    Context mContext;
    private ArrayList<UserDataModel> dataSet;

    CustomAdapter(ArrayList<UserDataModel> data, Context context) {
        super(context, R.layout.single_follow, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        UserDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        FixImagePosition holder = new FixImagePosition();


        // view lookup cache stored in tag
        ViewHolder viewHolder;
        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_follow, parent, false);

            holder.position = position;

            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.PicInfo = convertView.findViewById(R.id.item_Pic);
            holder.Cover = viewHolder.PicInfo;


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        assert dataModel != null;
        viewHolder.txtName.setText(dataModel.getName());
        CustomAdapter.GetImage Pic = new CustomAdapter.GetImage(position, holder);
        Pic.execute(dataModel.getPicLink());

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView PicInfo;
    }

    private static class FixImagePosition {
        ImageView Cover;
        public int position;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private FixImagePosition mBookCover;

        GetImage(int position, FixImagePosition holder) {
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
            if (mBookCover.position == mPosition) {
                mBookCover.Cover.setImageBitmap(result);
            }
        }
    }





}
