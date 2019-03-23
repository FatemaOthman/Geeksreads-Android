package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;

public class SignupActivity extends AppCompatActivity
{
    String FullNameStr;
    String EmailStr;
    String PasswordStr;
    Context mContext;
    public static String forTest;

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

        mContext = this;
        final EditText FullName = findViewById(R.id.UserNameTxt);
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

                if (FullNameStr.length() < 3 || FullNameStr.length() > 50)
                {
                    FullName.setError("Username length should be 3 characters minimum and 50 characters maximum");
                    forTest = "Username length should be 3 characters minimum and 50 characters maximum";
                }
                else if (!EmailStr.matches(".+[@].+[.].+"))
                {
                    Email.setError("Please enter a valid Email");
                    forTest = "Please enter a valid Email";
                }
                else if (PasswordStr.length() < 6)
                {
                    Password.setError("Password should be 6 characters or more");
                    Password.setText("");
                    ConfPassword.setText("");
                    forTest = "Password should be 6 characters or more";
                }
                else if (!PasswordStr.matches(".*[0-9].*"))
                {
                    Password.setError("Password should contain numbers");
                    Password.setText("");
                    ConfPassword.setText("");
                    forTest = "Password should contain numbers";
                }
                else if (!PasswordStr.matches(".*[a-z].*"))
                {
                    Password.setError("Password should contain lower case letters");
                    Password.setText("");
                    ConfPassword.setText("");
                    forTest = "Password should contain lower case letters";
                }
                else if (!PasswordStr.matches(".*[A-Z].*"))
                {
                    Password.setError("Password should contain upper case letters");
                    Password.setText("");
                    ConfPassword.setText("");
                    forTest = "Password should contain upper case letters";
                }
                else if (!PasswordStr.equals(ConfPassword.getText().toString()))
                {
                    ConfPassword.setError("Passwords don't match");
                    Password.setText("");
                    ConfPassword.setText("");
                    forTest = "Passwords don't match";
                }
                else
                {
                    //TODO Call Async Function
                    JSONObject JSON = new JSONObject();
                    try {
                        // TODO: Put all your JSON values Here.
                        JSON.put("UserName", FullNameStr);
                        JSON.put("UserEmail", EmailStr);
                        JSON.put("UserPassword", PasswordStr);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // TODO: Change the URL with your Service.
                    String UrlService = "http://geeksreads.000webhostapp.com/Morsy/Signup.php";

                    SignUp signUp = new SignUp();
                    signUp.execute(UrlService,JSON.toString());
                }

            }
        });

    }
    /**
     * Class that get the data from host and Add it to its views.
     *  The Parameters are host Url and toSend Data.
     */
    public class SignUp extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                String data = URLEncoder.encode("Json","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result){
            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

                final JSONObject jsonObject = new JSONObject(result);
                dialog.setTitle("Create account on GeeksReads");
                dialog.setMessage(jsonObject.getString("ReturnMsg"));
                forTest = jsonObject.getString("ReturnMsg");

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try {
                            if (jsonObject.getString("ReturnMsg").contains("A verification email has been sent"))
                            {
                                final EditText FullName = findViewById(R.id.UserNameTxt);
                                final EditText Email = findViewById((R.id.EmailTxt));
                                final EditText Password = findViewById(R.id.PasswordTxt);
                                final EditText ConfPassword = findViewById(R.id.ConfirmPasswordTxt);
                                FullName.setText("");
                                Email.setText("");
                                Password.setText("");
                                ConfPassword.setText("");

                                //Go to Sign in Layout
                                Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(myIntent);
                            }
                            else
                            {
                                //Stay Here
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.show();


            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }
}
