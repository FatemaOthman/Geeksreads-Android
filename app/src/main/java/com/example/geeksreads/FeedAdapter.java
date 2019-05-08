package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String sForTestLikeStatus;
    public static boolean sFortTestLikeBoolean;

    private ArrayList<FeedModel> dataSet;
    Context context;
    int total_types;
    private boolean fabStateVolume = false;
    public static class MyClickableSpan extends ClickableSpan {
        public static int MAKER_PROFILE=0;
        public static int COMMENT = 1;
        public static int REVIEW = 2;
        public static int BOOK = 3;
        public static int AUTHOR =4;
        private final String mText;
        private int type;
        private Context context;
        String ExtraText;

        private  MyClickableSpan(final String text, int type, Context mContext, String ExtraText) {
            mText = text;
            this.type=type;
            this.context=mContext;
            this.ExtraText=ExtraText;

        }
        @Override
        public void onClick(final View widget) {
            if(this.type==0)
            {
                Intent myintent = new Intent(context,OtherProfileActivity.class);
                myintent.putExtra("FollowId",ExtraText);
                context.startActivity(myintent);
            }
            else if(this.type ==1)
            {
                Intent intent = new Intent(context,Comments.class);
                intent.putExtra("ReviewId",ExtraText);
                context.startActivity(intent);

            }
            else
            {
                Intent myintent = new Intent(context,BookActivity.class);
                myintent.putExtra("BookID",ExtraText);
                context.startActivity(myintent);

            }

        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(Color.DKGRAY);
            ds.setElegantTextHeight(true);

        }

    }

    public static void  SetLink(int Type, String Original, FeedModel object, final Context mContext, RecyclerView.ViewHolder holder)
    {
        if(Type==0)//Comment

        {
            SpannableString ss = new SpannableString(Original);
            int StartIndex = Original.indexOf(object.getCommentMakerName());
            int End = StartIndex + object.getCommentMakerName().length();
            if(StartIndex!=-1&&StartIndex<End)
            ss.setSpan(new MyClickableSpan(object.getCommentMakerName(),0,mContext,object.getCommentMakerID()),StartIndex,End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            StartIndex = Original.indexOf("review");
            End = StartIndex+6;
            if(StartIndex!=-1&&StartIndex<End)
            ss.setSpan(new MyClickableSpan("review",1,mContext,object.getReviewID()),StartIndex,End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            StartIndex = Original.indexOf(object.getReviewMakerName());
            End =  StartIndex + object.getReviewMakerName().length();
            if(StartIndex!=-1&&StartIndex<End)
            ss.setSpan(new MyClickableSpan(object.getReviewMakerName(),0,mContext,object.getReviewMakerID()),StartIndex,End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            ((CommentViewHolder) holder).textViewPostBody.setText(ss);
            ((CommentViewHolder) holder).textViewPostBody.setClickable(true);
            ((CommentViewHolder) holder).textViewPostBody.setMovementMethod(LinkMovementMethod.getInstance());
            ((CommentViewHolder) holder).textViewPostBody.setHighlightColor(Color.TRANSPARENT);
        }
        else
        {
            SpannableString ss = new SpannableString(Original);
            int StartIndex = Original.indexOf(object.getReviewMakerName());
            int End = StartIndex + object.getReviewMakerName().length();
            if(StartIndex!=-1&&StartIndex<End)
            ss.setSpan(new MyClickableSpan(object.getReviewMakerName(),0,mContext,object.getReviewMakerID()),StartIndex,End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            StartIndex = Original.indexOf("reviewed");
            End = StartIndex+8;
            if(StartIndex!=-1&&StartIndex<End)
            ss.setSpan(new MyClickableSpan("reviewed",1,mContext,object.getReviewID()),StartIndex,End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            StartIndex = Original.indexOf(object.getBookName());
            End =  StartIndex + object.getBookName().length();
            if(StartIndex!=-1&&StartIndex<End)
            ss.setSpan(new MyClickableSpan(object.getBookName(),2,mContext,object.getBookID()),StartIndex,End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            ((ReviewViewHolder) holder).textViewPostBody.setText(ss);
            ((ReviewViewHolder) holder).textViewPostBody.setClickable(true);
            ((ReviewViewHolder) holder).textViewPostBody.setMovementMethod(LinkMovementMethod.getInstance());
            ((ReviewViewHolder) holder).textViewPostBody.setHighlightColor(Color.TRANSPARENT);


        }


    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewPostBody;
        public TextView TextViewPostTime;
        public ImageView imageViewPostPic;
        public TextView LikeText;
        public TextView CommentText;

        public CommentViewHolder(View itemView) {
            super(itemView);

            this.textViewPostBody= (TextView)(TextView) itemView.findViewById(R.id.postBody);
            this.TextViewPostTime = (TextView)itemView.findViewById(R.id.postTime);
            this.imageViewPostPic =(ImageView)itemView.findViewById(R.id.postPic);


        }

    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {


        public TextView textViewPostBody;
        public TextView TextViewPostTime;
        public ImageView imageViewPostPic;
        public TextView textViewBookName;
        public TextView textViewBookAuthor;
        public TextView textViewBookRating;
        public TextView textViewNumOfRatings;
        public RatingBar ratingBarBookRating;
        public ImageView imageViewBookCover;
        public TextView textViewReviewBody;
        public Button buttonBookState;
        public Button LikeText;
        public Button CommentText;


        public ReviewViewHolder(View itemView) {
            super(itemView);

            this.textViewPostBody= (TextView) itemView.findViewById(R.id.postBody);
            this.TextViewPostTime = (TextView)itemView.findViewById(R.id.postTime);
            this.imageViewPostPic =(ImageView)itemView.findViewById(R.id.postPic);
            textViewBookName = (TextView) itemView.findViewById(R.id.bookName);
            textViewBookAuthor=(TextView) itemView.findViewById(R.id.authorName);
            textViewBookRating = (TextView) itemView.findViewById(R.id.bookRating);
            textViewNumOfRatings=(TextView) itemView.findViewById(R.id.numOfRatings);
            ratingBarBookRating=(RatingBar) itemView.findViewById(R.id.bookRatingBar);
            imageViewBookCover=(ImageView) itemView.findViewById(R.id.bookCover);
            buttonBookState = (Button) itemView.findViewById(R.id.BookState);
            textViewReviewBody=(TextView)itemView.findViewById(R.id.ReviewBody);
            this.LikeText=(Button) itemView.findViewById(R.id.LikeReview);
            this.CommentText=(Button) itemView.findViewById(R.id.CommentonReview);







        }

    }


    public FeedAdapter(ArrayList<FeedModel> data, Context context) {
        this.dataSet = data;
        this.context = context;
        total_types = dataSet.size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case FeedModel.COMMENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false);
                return new CommentViewHolder(view);
            case FeedModel.REVIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_review_item, parent, false);
                return new ReviewViewHolder(view);
        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return FeedModel.COMMENT_TYPE;
            case 1:
                return FeedModel.REVIEW_TYPE;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        final FeedModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case FeedModel.COMMENT_TYPE:
                    ((CommentViewHolder) holder).textViewPostBody.setText(object.getPostBody());
                    ((CommentViewHolder) holder).TextViewPostTime.setText(object.getPostTime());
                    if(object.getCommentMakerPhoto().length()>0)
                    Picasso.with(context)
                            .load(object.getCommentMakerPhoto())
                            .into(((CommentViewHolder) holder).imageViewPostPic);
                    ((CommentViewHolder) holder).imageViewPostPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(context, OtherProfileActivity.class);
                            myIntent.putExtra("FollowId",object.getReviewMakerID());
                            context.startActivity(myIntent);

                        }
                    });
                    String Original = ((CommentViewHolder) holder).textViewPostBody.getText().toString();
                    SetLink(0,Original,object, context,holder);
                    break;
                case FeedModel.REVIEW_TYPE:
                    ((ReviewViewHolder) holder).textViewPostBody.setText(object.getPostBody());
                    ((ReviewViewHolder) holder).TextViewPostTime.setText(object.getPostTime());
                    ((ReviewViewHolder)holder).textViewBookAuthor.setText(object.getBookAuthor());
                    ((ReviewViewHolder)holder).textViewBookName.setText(object.getBookName());
                    if(object.isLiked()==true) {
                        ((ReviewViewHolder)holder).LikeText.setText("Liked");
                    }

                    else
                    {
                        ((ReviewViewHolder)holder).LikeText.setText("Like");
                    }

                    ((ReviewViewHolder)holder).LikeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(object.isLiked()==true)
                            {
                                UnLikeResource UnlikeResource = new UnLikeResource(holder);
                                String UrlLike= APIs.API_UNLIKE_REVIEW;
                                JSONObject jsonObject=new JSONObject();

                                try {
                                    jsonObject.put("token",UserSessionManager.getUserToken());
                                    jsonObject.put("User_Id",UserSessionManager.getUserID());
                                    jsonObject.put("resourceId",object.getReviewID());
                                    jsonObject.put("Type","Review");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                UnlikeResource.execute(UrlLike,jsonObject.toString());
                                ((ReviewViewHolder)holder).LikeText.setText("Like");
                                object.setIsLiked(false);
                                sForTestLikeStatus = "Like";
                                sFortTestLikeBoolean=false;


                            }
                            else
                            {
                                LikeResource likeResource = new LikeResource(holder);
                                String UrlUnLike= APIs.API_LIKE_REVIEW;
                                JSONObject jsonObject=new JSONObject();

                                try {
                                    jsonObject.put("token",UserSessionManager.getUserToken());
                                    jsonObject.put("User_Id",UserSessionManager.getUserID());
                                    jsonObject.put("resourceId",object.getReviewID());
                                    jsonObject.put("Type","Review");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                likeResource.execute(UrlUnLike,jsonObject.toString());
                                ((ReviewViewHolder)holder).LikeText.setText("Liked");
                                object.setIsLiked(true);
                                sForTestLikeStatus = "Liked";
                                sFortTestLikeBoolean=true;

                            }
                        }
                    });
                    ((ReviewViewHolder)holder).CommentText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,Comments.class);
                            intent.putExtra("ReviewId",object.getReviewID());
                            context.startActivity(intent);
                        }
                    });




                    String S = new String(object.getReviewBody());
                    ((ReviewViewHolder)holder).textViewReviewBody.setText(S);
                    if(object.getReviewMakerPhoto().length()>0)
                    Picasso.with(context)
                            .load(object.getReviewMakerPhoto())
                            .into(((ReviewViewHolder)holder).imageViewPostPic);
                    if(object.getBookPhoto().length()>0)
                    Picasso.with(context)
                            .load(object.getBookPhoto())
                            .into(((ReviewViewHolder)holder).imageViewBookCover);
                    String Original1 = ((ReviewViewHolder) holder).textViewPostBody.getText().toString();
                    SetLink(1,Original1,object, context,holder);

                    ((ReviewViewHolder)holder).buttonBookState.setText("Visit Book");
                    ((ReviewViewHolder)holder).buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.colorNotificationbar));


                    ((ReviewViewHolder)holder).buttonBookState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(context, BookActivity.class);
                            myIntent.putExtra("Author",object.getBookAuthor());
                            myIntent.putExtra("Title", object.getBookName());
                            myIntent.putExtra("Pages","320");
                            myIntent.putExtra("published","9-9-2011");
                            myIntent.putExtra("cover", object.getBookPhoto());
                            myIntent.putExtra("BookID",object.getBookID());
                            myIntent.putExtra("RatingNumber","30,500");
                            myIntent.putExtra("Rating","4.5");
                            context.startActivity(myIntent);


                        }
                    });
                    ((ReviewViewHolder)holder).imageViewBookCover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(context, BookActivity.class);
                            myIntent.putExtra("BookID",object.getBookID());
                            context.startActivity(myIntent);



                        }
                    });
                    ((ReviewViewHolder)holder).imageViewPostPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent myIntent = new Intent(context, OtherProfileActivity.class);
                            myIntent.putExtra("FollowId",object.getReviewMakerID());
                            context.startActivity(myIntent);


                        }
                    });
                    ((ReviewViewHolder)holder).textViewBookAuthor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(context, AuthorActivity.class);
                            myIntent.putExtra("AuthorID",object.getAuthorID());
                            context.startActivity(myIntent);


                        }
                    });


                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @SuppressLint("StaticFieldLeak")
    private class UnLikeResource extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
        RecyclerView.ViewHolder holder;
        public UnLikeResource(RecyclerView.ViewHolder holder)
        {
            this.holder=holder;
        }

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String newJson = params[1];

            String result = "";
            try {
                /* Create a URL object holding our url */
                URL url = new URL(UrlString);
                /* Create an HTTP Connection and adjust its options */
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("content-type", "application/json");

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                writer.write(newJson);
                writer.flush();
                writer.close();
                ops.close();

                /* A Stream object to get the returned data from API Call */
                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"Error Occurred.\"}";
                        break;
                    default:
                        break;
                }
                http.disconnect();
                return result;

            }
            /* Handling Exceptions */ catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(context, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            /* Creating a JSON Object to parse the data in */

            if(result.equals("unliked")) {
                ((ReviewViewHolder)holder).LikeText.setTypeface(((ReviewViewHolder)holder).LikeText.getTypeface(), Typeface.NORMAL);
                ((ReviewViewHolder)holder).LikeText.setText("Like");


            }


        }

    }

    @SuppressLint("StaticFieldLeak")
    private class LikeResource extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
        RecyclerView.ViewHolder holder;
        public LikeResource(RecyclerView.ViewHolder holder)
        {
            this.holder=holder;
        }

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String newJson = params[1];

            String result = "";
            try {
                /* Create a URL object holding our url */
                URL url = new URL(UrlString);
                /* Create an HTTP Connection and adjust its options */
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestProperty("content-type", "application/json");

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                writer.write(newJson);
                writer.flush();
                writer.close();
                ops.close();

                /* A Stream object to get the returned data from API Call */
                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"Error Occurred.\"}";
                        break;
                    default:
                        break;
                }
                http.disconnect();
                return result;

            }
            /* Handling Exceptions */ catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(context, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            /* Creating a JSON Object to parse the data in */
            //final JSONObject jsonObject = new JSONObject(result);
            if(result.equals("liked"))
            {

            }


        }

    }






}

