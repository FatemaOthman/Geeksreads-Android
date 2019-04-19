package com.example.geeksreads;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);
        Button loginButton = findViewById(R.id.loginBtn);
        Button signupButton = findViewById(R.id.signupBtn);

        /* Send Signout API */
        //todo
        /* Delete all user's data (id and token) */
        deleteUserSessionInformation();

        /* Sign up button command */
        signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SignOutActivity.this, SignupActivity.class);
                startActivity(myIntent);
            }
        });

        /* Sign in button command */
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SignOutActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    void deleteUserSessionInformation()
    {
        //todo
    }
}
