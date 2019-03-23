package com.example.geeksreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class FollowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_following);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Followers/ing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ViewPager viewPager =  findViewById(R.id.viewpager);
        System.out.println("View Pager Initialized");
        viewPager.setAdapter(new CustomPagerAdapter(this));
    }

}