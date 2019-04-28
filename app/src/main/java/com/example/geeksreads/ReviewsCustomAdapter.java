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

import com.example.geeksreads.views.ExpandableTextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ReviewsCustomAdapter extends ArrayAdapter<ReviewDataModel> implements View.OnClickListener {

    Context mContext;
    private ArrayList<ReviewDataModel> dataSet;

    ReviewsCustomAdapter(ArrayList<ReviewDataModel> data, Context context) {
        super(context, R.layout.post_item_list_view, data);
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
        ReviewDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        FixImagePosition holder = new FixImagePosition();


        // view lookup cache stored in tag
        ViewHolder viewHolder;
        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post_item_list_view, parent, false);

            holder.position = position;

            viewHolder.BookCoverPicture = convertView.findViewById(R.id.postImageView);
            viewHolder.UserPicInfo = convertView.findViewById(R.id.authorImageView);
            viewHolder.Details = convertView.findViewById(R.id.detailsTextView);
            viewHolder.UserName = convertView.findViewById(R.id.titleTextView);
            viewHolder.NLikes = convertView.findViewById(R.id.likeCounterTextView);
            viewHolder.NComments = convertView.findViewById(R.id.commentsCountTextView);

            holder.Cover = viewHolder.UserPicInfo;
            holder.BookC = viewHolder.BookCoverPicture;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        assert dataModel != null;
        viewHolder.Details.setText(dataModel.getReviewText());
        viewHolder.UserName.setText(dataModel.getUserName());
        viewHolder.NLikes.setText(dataModel.getNLikes());
        viewHolder.NComments.setText(dataModel.getNComments());
        viewHolder.ReviewID = dataModel.getReviewID();

        ReviewsCustomAdapter.GetBookCoverImage CoverPic = new ReviewsCustomAdapter.GetBookCoverImage(position, holder);
        CoverPic.execute(dataModel.getBookCoverPicture());

        ReviewsCustomAdapter.GetUserImage UserPic = new ReviewsCustomAdapter.GetUserImage(position, holder);
        UserPic.execute(dataModel.getUserProfilePicture());


        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView UserPicInfo;
        ImageView BookCoverPicture;
        ExpandableTextView Details;
        TextView UserName;
        TextView NLikes;
        TextView NComments;
        String ReviewID;
        String UserWhoWroteID;
    }

    private static class FixImagePosition {
        public int position;
        ImageView Cover;
        ImageView BookC;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    private class GetUserImage extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private FixImagePosition mUserCover;

        GetUserImage(int position, FixImagePosition holder) {
            mPosition = position;
            mUserCover = holder;
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
            if (mUserCover.position == mPosition) {
                mUserCover.Cover.setImageBitmap(result);
            }
        }
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    private class GetBookCoverImage extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private FixImagePosition mUserCover;

        GetBookCoverImage(int position, FixImagePosition holder) {
            mPosition = position;
            mUserCover = holder;
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
            if (mUserCover.position == mPosition) {
                mUserCover.BookC.setImageBitmap(result);
            }
        }
    }


}

