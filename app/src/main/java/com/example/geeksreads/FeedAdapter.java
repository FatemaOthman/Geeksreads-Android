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
    /**
     *onCreateViewHolder: Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an Feed item.
     * @param parent
     * @param viewType
     * @return ViewHolder : A new ViewHolder that holds a View of the given view type
     * */

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_list_item, parent, false);
        return new ViewHolder(v);
    }
    /**
     * onBindViewHolder: Called by RecyclerView to display the data at the specified position.
     * @param  holder
     * @param  position
     * @return void
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem feeditem = feedItemList.get(position);
        holder.textViewPostBody.setText(feeditem.getPostBody());
        holder.getTextViewPostTime.setText(feeditem.getPostTime());
        Picasso.with(context)
                .load(feeditem.getNotifierPicURL())
                .into(holder.imageViewPostPic);
    }
    /**
     * getItemCount: How many items are in the data set represented by this Adapter.
     * @return Number of Items.
     * */
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
