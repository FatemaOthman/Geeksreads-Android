package com.example.geeksreads;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{

    @NonNull
    private List<BookItem> bookItemList;
    private Context context;

    public BookAdapter(@NonNull List<BookItem> bookItemList, Context context) {
        this.bookItemList = bookItemList;
        this.context = context;
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
                .inflate(R.layout.author_books_list_item,parent,false);
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
        final BookItem bookitem = bookItemList.get(position);
        holder.textViewBookAuthor.setText(bookitem.getBookAuthor());
        holder.textViewBookName.setText(bookitem.getBookName());
        holder.textViewBookRating.setText(bookitem.getBookRate());
        holder.textViewNumOfRatings.setText(bookitem.getNumOfRates());
        holder.ratingBarBookRating.setRating(Float.parseFloat((String) bookitem.getBookRate()));
        Picasso.with(context)
                .load(bookitem.getBookCoverURL())
                .into(holder.imageViewBookCover);


        if (bookitem.getStatus().equals("Read")) {
            holder.buttonBookState.setText("Read");
            holder.buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.ReadColor));
        } else if (bookitem.getStatus().equals("Want to Read")) {
            holder.buttonBookState.setText("Want To Read");
            holder.buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.WantToReadColor));
        } else if (bookitem.getStatus().equals("Currently Reading")) {
            holder.buttonBookState.setText("Currently Reading");
            holder.buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.ReadingColor));
        } else {
            holder.buttonBookState.setText("Add to shelf");
            holder.buttonBookState.setBackgroundColor(ContextCompat.getColor(context, R.color.colorNotificationbar));
        }

        holder.buttonBookState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, ChooseShelfActivity.class);
                myIntent.putExtra("Author",bookitem.getBookAuthor());
                myIntent.putExtra("Title", bookitem.getBookName());
                myIntent.putExtra("Rating",bookitem.getBookRate());
                myIntent.putExtra("Pages","3");
                myIntent.putExtra("published","9-9-2011");
                myIntent.putExtra("cover", bookitem.getBookCoverURL());
                context.startActivity(myIntent);


            }
        });
        holder.imageViewBookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, BookActivity.class);
                context.startActivity(myIntent);



            }
        });




    }
    /**
     * getItemCount: How many items are in the data set represented by this Adapter.
     * @return Number of Items.
     * */
    @Override
    public int getItemCount() {
        return bookItemList.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewBookName;
        public TextView textViewBookAuthor;
        public TextView textViewBookRating;
        public TextView textViewNumOfRatings;
        public RatingBar ratingBarBookRating;
        public ImageView imageViewBookCover;
        public Button buttonBookState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookName = (TextView) itemView.findViewById(R.id.bookName);
            textViewBookAuthor=(TextView) itemView.findViewById(R.id.authorName);
            textViewBookRating = (TextView) itemView.findViewById(R.id.bookRating);
            textViewNumOfRatings=(TextView) itemView.findViewById(R.id.numOfRatings);
            ratingBarBookRating=(RatingBar) itemView.findViewById(R.id.bookRatingBar);
            imageViewBookCover=(ImageView) itemView.findViewById(R.id.bookCover);
            buttonBookState = (Button) itemView.findViewById(R.id.BookState);

        }
    }

}
