package com.example.geeksreads;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class FollowActivity extends AppCompatActivity {


    private static String CurrentUserID;
    private Follow_Adapter mSectionsPageAdapter;

    Context mContext;
    private ViewPager mViewPager;

    /**
     * Function that returns the current user ID.
     *
     * @return CurrentUserID
     */
    public static String getCurrentID() {
        return CurrentUserID;
    }


    /**
     * onCreate: gets viewPager and fills it with the fragments (Follower_Fragment / Following_Fragment)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_following);
        CurrentUserID = getIntent().getStringExtra("UserID");
        ////////////////////////////////////////////////////////////
        mContext = this;

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Followers/ing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /////////////////////////////////////////////////////////////
        mSectionsPageAdapter = new Follow_Adapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * setupViewPager: Adds Fragments to the Follow_Adapter
     *           then  Adds Adapter to ViewPager
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        Follow_Adapter adapter = new Follow_Adapter(getSupportFragmentManager());
        adapter.addFragment(new Followers_Fragment(), "Followers");
        adapter.addFragment(new Following_Fragment(), "Following");
        viewPager.setAdapter(adapter);
    }


}
