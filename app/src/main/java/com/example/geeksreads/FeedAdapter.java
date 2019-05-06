package com.example.geeksreads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


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
                /*Intent myintent = new Intent(context,Review.class);
                myintent.putExtra("ReviewID",ExtraText);
                context.startActivity(myintent);*/

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
          //  ds.setFakeBoldText(true);
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
                    String Original = ((CommentViewHolder) holder).textViewPostBody.getText().toString();
                    SetLink(0,Original,object, context,holder);
                    break;
                case FeedModel.REVIEW_TYPE:
                    ((ReviewViewHolder) holder).textViewPostBody.setText(object.getPostBody());
                    ((ReviewViewHolder) holder).TextViewPostTime.setText(object.getPostTime());
                    ((ReviewViewHolder)holder).textViewBookAuthor.setText(object.getBookAuthor());
                    ((ReviewViewHolder)holder).textViewBookName.setText(object.getBookName());
                    String S = new String(object.getReviewBody());
                    S=S.substring(0,S.length()>120?120:S.length()-1);
                    if(object.getReviewMakerPhoto().length()>0)
                    ((ReviewViewHolder)holder).textViewReviewBody.setText(S);
                    Picasso.with(context)
                            .load(object.getReviewMakerPhoto())
                            .into(((ReviewViewHolder)holder).imageViewPostPic);
                    if(object.getBookPhoto().length()>0)
                    Picasso.with(context)
                            .load(object.getBookPhoto())
                            .into(((ReviewViewHolder)holder).imageViewBookCover);
                    String Original1 = ((ReviewViewHolder) holder).textViewPostBody.getText().toString();
                    SetLink(1,Original1,object, context,holder);



                    if (object.getBookStatue().equals("Read")) {
                        ((ReviewViewHolder)holder).buttonBookState.setText("Read");
                        ((ReviewViewHolder)holder).buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.ReadColor));
                    } else if (object.getBookStatue().equals("Want to Read")) {
                        ((ReviewViewHolder)holder).buttonBookState.setText("Want To Read");
                        ((ReviewViewHolder)holder).buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.WantToReadColor));
                    } else if (object.getBookStatue().equals("Currently Reading")) {
                        ((ReviewViewHolder)holder).buttonBookState.setText("Currently Reading");
                        ((ReviewViewHolder)holder).buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.ReadingColor));
                    } else {
                        ((ReviewViewHolder)holder).buttonBookState.setText("Add to shelf");
                        ((ReviewViewHolder)holder).buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.colorNotificationbar));
                    }

                    ((ReviewViewHolder)holder).buttonBookState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(context, ChooseShelfActivity.class);
                            myIntent.putExtra("Author",object.getBookAuthor());
                            myIntent.putExtra("Title", object.getBookName());
                            myIntent.putExtra("Pages","3");
                            myIntent.putExtra("published","9-9-2011");
                            myIntent.putExtra("cover", object.getBookPhoto());
                            myIntent.putExtra("BookID",object.getBookID());
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

}

