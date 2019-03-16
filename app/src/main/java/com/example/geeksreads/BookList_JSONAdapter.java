package com.example.geeksreads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookList_JSONAdapter extends BaseAdapter{

    private JSONArray data;
    private final Context context;

    public  BookList_JSONAdapter(Context context,JSONArray data){
        this.data = data;
        this.context = context;
    }

    public int getCount() {
        return data.length();
    }

    public Object getItem(int i) {

        try{
            return data.getJSONObject(i);
        }catch(JSONException jse){
            jse.printStackTrace();
        }

        return null;
    }

    public long getItemId(int i) {

        try{
            JSONObject object = data.getJSONObject(i);
            return object.getLong("id");
        }catch(JSONException jse){
            jse.printStackTrace();
        }
        return -1;
    }

    // returns the view of a single row

    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inflater.inflate(R.layout.book_template, viewGroup,false);
        try {
            TextView BookName = itemView.findViewById(R.id.BookNameTxt);
            BookName.setText(data.getJSONObject(i).getString("bookname"));

            TextView AuthorName = itemView.findViewById(R.id.ByAuthorNameTxt);
            AuthorName.setText(data.getJSONObject(i).getString("value"));

            TextView RatingNumber = itemView.findViewById(R.id.BookRatingsTxt);
            RatingNumber.setText(data.getJSONObject(i).getString("value"));

            TextView BookDescription = itemView.findViewById(R.id.SomeDesc);
            BookDescription.setText(data.getJSONObject(i).getString("value"));

            ImageView BookCover = itemView.findViewById(R.id.BookImage);
            //BookCover.getDrawable(data.getJSONObject(i).getString("value"))


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return itemView;
    }
}
