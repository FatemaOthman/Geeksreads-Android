package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geeksreads.views.ExpandableTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;


/**
 * Reviews Custom Adapter: Adapter Class for Displaying A Book Reviews from the database.
 */
public class ReviewsCustomAdapter extends ArrayAdapter<ReviewDataModel> implements View.OnClickListener {
    public static String sForTestLikeReview;
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
        final ReviewDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        FixImagePosition holder = new FixImagePosition();


        // view lookup cache stored in tag
        final ViewHolder viewHolder;
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
            viewHolder.Comments = convertView.findViewById(R.id.commentsCountImageView);
            viewHolder.Likes = convertView.findViewById(R.id.likesImageView);
            viewHolder.Rating = convertView.findViewById(R.id.ReviewRatingBar);
            viewHolder.DateOfReview = convertView.findViewById(R.id.DateOfReview);


            holder.Cover = viewHolder.UserPicInfo;
            holder.BookC = viewHolder.BookCoverPicture;
            holder.LikesHolder = viewHolder.Likes;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        assert dataModel != null;
        viewHolder.Details.setText(dataModel.getReviewText());
        viewHolder.UserName.setText(dataModel.getUserName());
        viewHolder.NLikes.setText(dataModel.getNLikes());
        viewHolder.NComments.setText(dataModel.getNComments());
        viewHolder.DateOfReview.setText(dataModel.getReviewDate());

        viewHolder.ReviewID = dataModel.getReviewID();
        viewHolder.UserWhoWroteID = dataModel.getUserWhoWroteID();
        viewHolder.IsLiked = dataModel.getLikeStatus();

        viewHolder.Rating.setNumStars(5);
        viewHolder.Rating.setRating(Float.parseFloat(dataModel.getReviewRating()));

        if (viewHolder.IsLiked.equals("true")) {
            viewHolder.Likes.setImageResource(R.drawable.ic_like_active);


        } else {

            viewHolder.Likes.setImageResource(R.drawable.ic_like);
        }


        ReviewsCustomAdapter.GetBookCoverImage CoverPic = new ReviewsCustomAdapter.GetBookCoverImage(position, holder);
        CoverPic.execute(dataModel.getBookCoverPicture());
        Log.d("AMR", "dataModel.getBookCoverPicture(): " + dataModel.getBookCoverPicture());

        ReviewsCustomAdapter.GetUserImage UserPic = new ReviewsCustomAdapter.GetUserImage(position, holder);
        UserPic.execute(dataModel.getUserProfilePicture());

        viewHolder.UserPicInfo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dataModel.getUserWhoWroteID().equals(UserSessionManager.getUserID())) {
                            Intent myIntent = new Intent(mContext, OtherProfileActivity.class);
                            myIntent.putExtra("FollowId", dataModel.getUserWhoWroteID());
                            mContext.startActivity(myIntent);
                        } else {

                            Intent myIntent = new Intent(mContext, Profile.class);
                            mContext.startActivity(myIntent);

                        }
                    }
                }
        );

        viewHolder.NComments.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(mContext, Comments.class);
                        myIntent.putExtra("ReviewId", dataModel.getReviewID());
                        myIntent.putExtra("BookId", dataModel.getBookID());
                        myIntent.putExtra("Photo", dataModel.getUserProfilePicture());
                        mContext.startActivity(myIntent);
                    }
                }
        );


        viewHolder.Comments.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(mContext, Comments.class);
                        myIntent.putExtra("ReviewId", dataModel.getReviewID());
                        myIntent.putExtra("BookId", dataModel.getBookID());
                        mContext.startActivity(myIntent);
                    }
                }
        );


        final JSONObject LikeJson = new JSONObject();
        try {
            LikeJson.put("token", UserSessionManager.getUserToken());
            LikeJson.put("Type", "Review");
            LikeJson.put("resourceId", viewHolder.ReviewID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String LikeRequest = APIs.API_LIKE_REVIEW;
        final String UnLikeRequest = APIs.API_UNLIKE_REVIEW;

        viewHolder.Likes.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {

                        if (viewHolder.IsLiked.equals("false")) { //Review wasn't already liked

                            ReviewsCustomAdapter.LikeReview TestObject = new ReviewsCustomAdapter.LikeReview();
                            TestObject.execute(LikeRequest, LikeJson.toString());
                            viewHolder.Likes.setImageResource(R.drawable.ic_like_active);
                            viewHolder.IsLiked = "true";
                            viewHolder.NLikes.setText(Integer.toString(Integer.parseInt(viewHolder.NLikes.getText().toString()) + 1));

                        } else { //Review was already liked

                            ReviewsCustomAdapter.UnLikeReview TestObject = new ReviewsCustomAdapter.UnLikeReview();
                            TestObject.execute(UnLikeRequest, LikeJson.toString());
                            viewHolder.Likes.setImageResource(R.drawable.ic_like);
                            viewHolder.IsLiked = "false";
                            viewHolder.NLikes.setText(Integer.toString(Integer.parseInt(viewHolder.NLikes.getText().toString()) - 1));
                        }

                    }
                }
        );


        viewHolder.NLikes.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {

                        if (viewHolder.IsLiked.equals("false")) { //Review wasn't already liked

                            ReviewsCustomAdapter.LikeReview TestObject = new ReviewsCustomAdapter.LikeReview();
                            TestObject.execute(LikeRequest, LikeJson.toString());
                            viewHolder.Likes.setImageResource(R.drawable.ic_like_active);
                            viewHolder.IsLiked = "true";
                            viewHolder.NLikes.setText(Integer.toString(Integer.parseInt(viewHolder.NLikes.getText().toString()) + 1));

                        } else { //Review was already liked

                            ReviewsCustomAdapter.UnLikeReview TestObject = new ReviewsCustomAdapter.UnLikeReview();
                            TestObject.execute(UnLikeRequest, LikeJson.toString());
                            viewHolder.Likes.setImageResource(R.drawable.ic_like);
                            viewHolder.IsLiked = "false";
                            viewHolder.NLikes.setText(Integer.toString(Integer.parseInt(viewHolder.NLikes.getText().toString()) - 1));
                        }

                    }
                }
        );



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
        ImageView Likes;
        String IsLiked;
        ImageView Comments;
        RatingBar Rating;
        TextView DateOfReview;
    }

    private static class FixImagePosition {
        public int position;
        ImageView Cover;
        ImageView BookC;
        ImageView LikesHolder;
    }

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    @SuppressLint("StaticFieldLeak")
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

    /**
     * Class that get image from Url and Add it to ImageView.
     * The only Parameter is the Url.
     */
    @SuppressLint("StaticFieldLeak")
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
                return BitmapFactory.decodeStream(input);
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

    @SuppressLint("StaticFieldLeak")
    private class LikeReview extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public boolean TestSucc = false;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            StringBuilder result = new StringBuilder();

            try {
                /* Create a URL object holding our url */
                URL url = new URL(UrlString);
                /* Create an HTTP Connection and adjust its options */
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("content-type", "application/json");
                http.setRequestProperty("x-auth-token", UserSessionManager.getUserToken());

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                writer.write(JSONString);
                writer.flush();
                writer.close();
                ops.close();

                switch (String.valueOf(http.getResponseCode())) {
                    case "200":
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line;
                        //boolean started = false;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        reader.close();
                        ips.close();
                        sForTestLikeReview = "true";
                        TestSucc = true;
                        break;
                    default:
                        result = new StringBuilder("{\"ReturnMsg\":\"An Error Occurred!\"}");
                        break;
                }

                http.disconnect();
                return result.toString();

            }
            /* Handling Exceptions */ catch (MalformedURLException e) {
                result = new StringBuilder(e.getMessage());
            } catch (IOException e) {
                result = new StringBuilder(e.getMessage());
            }
            return result.toString();
        }

        /**
         * onPostExecute: Takes the string result and treates it as a json object
         * to set data of:
         * -Follow Button
         *
         * @param result : The result containing all the passed data from backend.
         */
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TestSucc) {
                Log.d("AMR", "Like is successful");
            }
                dialog.setMessage("Done");
                //dialog.show();

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class UnLikeReview extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        boolean TaskSucc = false;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            StringBuilder result = new StringBuilder();

            try {
                /* Create a URL object holding our url */
                URL url = new URL(UrlString);
                /* Create an HTTP Connection and adjust its options */
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("content-type", "application/json");
                http.setRequestProperty("x-auth-token", UserSessionManager.getUserToken());

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                writer.write(JSONString);
                writer.flush();
                writer.close();
                ops.close();

                switch (String.valueOf(http.getResponseCode())) {
                    case "200":
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line;
                        //boolean started = false;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        reader.close();
                        ips.close();
                        TaskSucc = true;
                        break;
                    default:
                        result = new StringBuilder("{\"ReturnMsg\":\"An Error Occurred!\"}");
                        break;
                }

                http.disconnect();
                return result.toString();

            }
            /* Handling Exceptions */ catch (MalformedURLException e) {
                result = new StringBuilder(e.getMessage());
            } catch (IOException e) {
                result = new StringBuilder(e.getMessage());
            }
            return result.toString();
        }

        /**
         * onPostExecute: Takes the string result and treates it as a json object
         * to set data of:
         * -Follow Button
         *
         * @param result : The result containing all the passed data from backend.
         */
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TaskSucc) {
                Log.d("AMR", "Unliked Succesfully");
            }
            dialog.setMessage("Done");
                //dialog.show();

        }

    }

}

