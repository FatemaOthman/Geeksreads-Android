package com.example.geeksreads;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SearchHandlerAdapter extends RecyclerView.Adapter<SearchHandlerAdapter.ViewHolder>{
    @NonNull
    private List<SearchHandlerItem> searchHandlerItemList;
    private Context context;

    public SearchHandlerAdapter( @NonNull List<SearchHandlerItem> searchHandlerItemList, Context context) {
        this.searchHandlerItemList = searchHandlerItemList;
        this.context = context;
    }
    public void clear() {
        final int size = searchHandlerItemList.size();
        searchHandlerItemList.clear();
        notifyItemRangeRemoved(0, size);
    }



    /**
     *onCreateViewHolder: Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     * @param parent
     * @param viewType
     * @return ViewHolder : A new ViewHolder that holds a View of the given view type
     * */
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_handler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHandlerAdapter.ViewHolder holder, int position) {
        final SearchHandlerItem searchHandlerItem = searchHandlerItemList.get(position);
        holder.textViewBookAuthor.setText(searchHandlerItem.getBookAuthor());
        holder.textViewBookName.setText(searchHandlerItem.getBookName());
        holder.linearLayoutSearchHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, BookActivity.class);
                myIntent.putExtra("BookID",searchHandlerItem.getBookID());
                context.startActivity(myIntent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return searchHandlerItemList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewBookName;
        public TextView textViewBookAuthor;
        public LinearLayout linearLayoutSearchHandler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookName = (TextView) itemView.findViewById(R.id.BookNameInSearchHandler);
            textViewBookAuthor=(TextView) itemView.findViewById(R.id.AuthorNameInSearchHandler);
            linearLayoutSearchHandler=(LinearLayout)itemView.findViewById(R.id.SearchHandlerLinearLayout);

        }
    }

}

