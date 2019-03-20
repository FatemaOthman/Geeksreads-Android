package com.example.geeksreads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);



        TextView Followers= findViewById(R.id.ActualFollowersCount);
        Followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this,Followers.class);
                startActivity(myIntent);
            }
        });

        TextView Following= findViewById(R.id.ActualFollowingCount);
        Following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this,Following.class);
                startActivity(myIntent);
            }
        });


        //////////////////////////////////////////////////////
        //Strings and Variables:

        String ActualFollowers = null; //Will be initialized from the database using the api.
        String ActualFollowing = null;
        int ImageIndex =1; //Dummy value that will be changed.

        //Get Number of Followers and Following:
        Followers.setText(ActualFollowers);
        Following.setText(ActualFollowing);






        //////////////////////////////////////////////////////
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ImageView UserProfilePicture = findViewById(R.id.UserProfilePhoto);
        UserProfilePicture.setImageResource(ImageIndex);
    }


}
