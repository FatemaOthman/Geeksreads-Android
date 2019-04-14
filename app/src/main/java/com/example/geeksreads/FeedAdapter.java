package com.example.geeksreads;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    @NonNull
    private List<FeedItem> feedItemList;
    private Context context;

    public FeedAdapter(@NonNull List<FeedItem> feedItemList, Context context) {
        this.feedItemList = feedItemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem feeditem = feedItemList.get(position);
        holder.textViewPostBody.setText(feeditem.getPostBody());
        holder.getTextViewPostTime.setText(feeditem.getPostTime());
        Picasso.with(context)
                .load(feeditem.getNotifierPicURL())
                .into(holder.imageViewPostPic);
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       public TextView textViewPostBody;
       public TextView getTextViewPostTime;
       public ImageView imageViewPostPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPostBody=(TextView) itemView.findViewById(R.id.postBody);
            getTextViewPostTime=(TextView)itemView.findViewById(R.id.postTime);
            imageViewPostPic=(ImageView)itemView.findViewById(R.id.postPic);


        }
    }
}
