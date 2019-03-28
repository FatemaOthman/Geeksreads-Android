package com.example.geeksreads;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/*
public class FollowActivity extends AppCompatActivity {

    private static String CurrentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_following);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Followers/ing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CurrentUserID = getIntent().getStringExtra("UserID");


        ViewPager viewPager =  findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
    }


    public static String getCurrentID(){
        return CurrentUserID;
    }

}
*/


public class FollowActivity extends AppCompatActivity {


    private static String CurrentUserID;
    private Follow_Adapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    public static String getCurrentID() {
        return CurrentUserID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_following);
        CurrentUserID = getIntent().getStringExtra("UserID");

        mSectionsPageAdapter = new Follow_Adapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Follow_Adapter adapter = new Follow_Adapter(getSupportFragmentManager());
        adapter.addFragment(new Followers_Fragment(), "Followers");
        adapter.addFragment(new Followers_Fragment(), "Following");
        viewPager.setAdapter(adapter);
    }
}
