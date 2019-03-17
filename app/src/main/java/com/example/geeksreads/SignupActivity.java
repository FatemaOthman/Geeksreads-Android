package com.example.geeksreads;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity
{
    String FullNameStr;
    String EmailStr;
    String PasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button loginLink = findViewById(R.id.OrLoginLinkBtn);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

        final EditText FullName = findViewById(R.id.FullNameTxt);
        final EditText Email = findViewById((R.id.EmailTxt));
        final EditText Password = findViewById(R.id.PasswordTxt);
        final EditText ConfPassword = findViewById(R.id.ConfirmPasswordTxt);

        Button SignUp = findViewById(R.id.SignupBtn);
        SignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FullNameStr = FullName.getText().toString();
                EmailStr = Email.getText().toString();
                PasswordStr = Password.getText().toString();

                if (FullNameStr.isEmpty())
                {
                    FullName.setError("Please Enter your Full Name");
                }
                else if (!PasswordStr.equals(ConfPassword.getText().toString()))
                {
                    Password.setError("Passwords don't match");
                    ConfPassword.setError("Passwords don't match");
                    Password.setText("");
                    ConfPassword.setText("");
                }
                else if (!PasswordStr.isEmpty())
                {
                    Password.setError("Password can't be empty");
                    ConfPassword.setError("Password can't be empty");
                    Password.setText("");
                    ConfPassword.setText("");
                }
                else if (PasswordStr.length() > 8)
                {
                    Password.setError("Password should be 8 characters or more");
                    ConfPassword.setError("Password should be 8 characters or more");
                    Password.setText("");
                    ConfPassword.setText("");
                }
                else if (!PasswordStr.matches("[0-9]"))
                {
                    Password.setError("Password should contain numbers");
                    ConfPassword.setError("Password should contain numbers");
                    Password.setText("");
                    ConfPassword.setText("");
                }
                else if (!PasswordStr.matches("[a-z]"))
                {
                    Password.setError("Password should contain letters");
                    ConfPassword.setError("Password should contain letters");
                    Password.setText("");
                    ConfPassword.setText("");
                }
                else if (!PasswordStr.matches("[A-Z]"))
                {
                    Password.setError("Password should contain upper case letters");
                    ConfPassword.setError("Password should contain upper case letters");
                    Password.setText("");
                    ConfPassword.setText("");
                }
                else
                {
                    //TODO Call Async Function
                }

            }
        });

    }
}
