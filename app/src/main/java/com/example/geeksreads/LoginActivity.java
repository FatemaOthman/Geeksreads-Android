package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity
{
    String LoginEmailStr, LoginPasswordStr;
    Context mContext;
    String CurrentToken,CurrentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

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
                    JSONObject JSON = new JSONObject();
                    try {
                        // TODO: Put all your JSON values Here.
                        JSON.put("UserEmail", LoginEmailStr);
                        JSON.put("UserPassword", LoginPasswordStr);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // TODO: Change the URL with your Service.
                    String UrlService = "http://geeksreads.000webhostapp.com/Morsy/Signin.php";

                    SignIn signIn = new SignIn();
                    signIn.execute(UrlService,JSON.toString());
                }
            }
        });

    }
    /**
     * Class that get the data from host and Add it to its views.
     *  The Parameters are host Url and toSend Data.
     */
    public class SignIn extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
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

                // TODO: Add your Post Execute logic here.
                JSONObject jsonObject = new JSONObject(result);
                dialog.setTitle("Create account on GeeksReads");
                dialog.setMessage(jsonObject.getString("ReturnMsg"));
                dialog.show();
                if (jsonObject.getString("ReturnMsg").contains("Login Succeeded"))
                {
                    //Go to Home Layout
                    final EditText Email = findViewById((R.id.EmailTxt));
                    final EditText Password = findViewById(R.id.PasswordTxt);
                    Email.setText("");
                    Password.setText("");
                    CurrentToken = jsonObject.getString("ReturnToken");

                    //Go to Sign in Layout
                    Intent myIntent = new Intent(LoginActivity.this, NotificationActivity.class);
                    startActivity(myIntent);
                }
                else
                {
                    //Stay Here
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }
}
