package com.example.geeksreads;
import android.content.Context;
import android.support.annotation.NonNull;
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

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.author_books_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookItem bookitem = bookItemList.get(position);
        holder.textViewBookAuthor.setText(bookitem.getBookAuthor());
        holder.textViewBookName.setText(bookitem.getBookName());
        holder.textViewBookRating.setText(bookitem.getBookRate());
        holder.textViewNumOfRatings.setText(bookitem.getNumOfRates());
        holder.ratingBarBookRating.setRating(Float.parseFloat((String)bookitem.getBookRate()));
        Picasso.with(context)
                .load(bookitem.getBookCoverURL())
                .into(holder.imageViewBookCover);

        /*
        *  if ( bookitem.getStatus().equals("Read"))
                {
                    bookOptions.setText("Read");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.ReadColor));
                }
                else if ( bookitem.getStatus().equals("Want to Read"))
                {
                    bookOptions.setText("Want To Read");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.WantToReadColor));
                }
                else if ( bookitem.getStatus().equals("Currently Reading"))
                {
                    bookOptions.setText("Currently Reading");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.ReadingColor));
                }
                else
                {
                    bookOptions.setText("Add to shelf");
                    bookOptions.setBackgroundColor(getResources().getColor(R.color.colorNotificationbar));
                }
*/
       /* holder.buttonBookState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/



    }

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
