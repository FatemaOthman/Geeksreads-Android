package com.example.geeksreads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity
{
    String LoginEmailStr, LoginPasswordStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button LoginButton = findViewById(R.id.LoginBtn);
        final EditText LoginMail =  findViewById(R.id.EmailTxt);
        final EditText LoginPassword = findViewById(R.id.PasswordTxt);

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginEmailStr = LoginMail.getText().toString();
                LoginPasswordStr = LoginPassword.getText().toString();

                if (LoginEmailStr.isEmpty())
                {
                    LoginMail.setError("Please enter your Geeksreads login Email");
                }
                else if (LoginPasswordStr.isEmpty())
                {
                    LoginPassword.setError("Please enter your Geeksreads Login Password");
                }
                else
                {
                    //TODO Call Async Login Function
                }
            }
        });



    }
}
