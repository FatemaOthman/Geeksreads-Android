package com.example.geeksreads;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<FeedItem> list;
    ImageView postPhoto;
    Context mContext;
    TextView postBody;
    TextView postTime;
    String postPhotoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        recyclerView = (RecyclerView) findViewById(R.id.FeedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContext = this;
        postBody=findViewById(R.id.postBody);
        postTime=findViewById(R.id.postTime);
        postPhoto=findViewById(R.id.postPic);
        list = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            FeedItem feedItem=new FeedItem(
                    "This is post number "+i,
                    i+" minutes ago.",
                    "http:\\/\\/geeksreads.000webhostapp.com\\/Fatema\\/prideandprejudice.jpg"

            );
            list.add(feedItem);
            adapter=new FeedAdapter(list,this);
            recyclerView.setAdapter(adapter);
        }


    }
}