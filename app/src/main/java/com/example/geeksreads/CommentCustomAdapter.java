package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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


public class CommentCustomAdapter  extends ArrayAdapter<CommentDataModel> implements View.OnClickListener {
    Context mContext;
    private ArrayList<CommentDataModel> dataSet;

    CommentCustomAdapter(ArrayList<CommentDataModel> data, Context context) {
        super(context, R.layout.comment_list_item, data);
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
        final CommentDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        CommentCustomAdapter.FixImagePosition holder = new CommentCustomAdapter.FixImagePosition();


        // view lookup cache stored in tag
        CommentCustomAdapter.ViewHolder viewHolder;
        if (convertView == null) {


            viewHolder = new CommentCustomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_list_item, parent, false);

            holder.position = position;

            viewHolder.UserPicInfo = convertView.findViewById(R.id.authorImageView);
            viewHolder.Details = convertView.findViewById(R.id.detailsTextView);
            viewHolder.UserName = convertView.findViewById(R.id.titleTextView);
            //   viewHolder.NLikes = convertView.findViewById(R.id.likeCounterTextView);

            holder.Cover = viewHolder.UserPicInfo;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommentCustomAdapter.ViewHolder) convertView.getTag();
        }


        assert dataModel != null;
        viewHolder.Details.setText(dataModel.getCommentText());
        viewHolder.UserName.setText(dataModel.getUserName());
        // viewHolder.NLikes.setText(dataModel.getNLikes());
        viewHolder.ReviewID = dataModel.getCommentID();
        viewHolder.UserWhoWroteID = dataModel.getUserWhoWroteID();

        CommentCustomAdapter.GetUserImage UserPic = new CommentCustomAdapter.GetUserImage(position, holder);
        UserPic.execute(dataModel.getUserProfilePicture());

        viewHolder.UserPicInfo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(mContext, OtherProfileActivity.class);
                        myIntent.putExtra("FollowId", dataModel.getUserWhoWroteID());
                        mContext.startActivity(myIntent);
                    }
                }
        );


        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView UserPicInfo;
        ExpandableTextView Details;
        TextView UserName;
        //  TextView NLikes;
        String ReviewID;
        String UserWhoWroteID;
    }

    private static class FixImagePosition {
        public int position;
        ImageView Cover;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetUserImage extends AsyncTask<String, Void, Bitmap> {
        private int mPosition;
        private CommentCustomAdapter.FixImagePosition mUserCover;

        GetUserImage(int position, CommentCustomAdapter.FixImagePosition holder) {
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
                return BitmapFactory.decodeStream(input);
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
}
