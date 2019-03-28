package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    /**
     * Global Variables to Store Returned Login Token and User ID
     */
    public static String sCurrentToken, sCurrentUserID;
    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest;
    /**
     * Global Private Variables to Store Login Email and Login Password from Text boxes
     */
    private String loginEmailStr;
    private String loginPasswordStr;
    /**
     * Global Variables to Store Context of this Activity itself
     */
    private Context mContext;

    /**
     * Function for Starting Logic Actions after Creating the Layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*For Displaying the toolbar on the top of Login Layout */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        /* Getting Text boxes and Buttons from the layout */
        Button loginButton = findViewById(R.id.LoginBtn);
        final EditText loginMail = findViewById(R.id.EmailTxt);
        final EditText loginPassword = findViewById(R.id.PasswordTxt);


        /* Function Handler for Clicking on Login Button, to Start Checking input Field
           and Sending JSON String to the Backend Login API
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmailStr = loginMail.getText().toString();
                loginPasswordStr = loginPassword.getText().toString();

                /* If the user entered an invalid Email Address */
                if (!loginEmailStr.matches(".+[@].+[.].+")) {
                    loginMail.setError("Please enter a valid Email");
                    sForTest = "Please enter a valid Email";
                }
                /* If the user entered an empty Password */
                else if (loginPasswordStr.isEmpty()) {
                    loginPassword.setError("Please enter your Geeksreads Login Password");
                    sForTest = "Please enter your Geeksreads Login Password";
                }
                /* If the user entered a valid email and password */
                else {
                    JSONObject mJSON = new JSONObject();
                    try {
                        mJSON.put("UserEmail", loginEmailStr);
                        mJSON.put("UserPassword", loginPasswordStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /* URL For Login API */
                    String urlService = "http://geeksreads.000webhostapp.com/Morsy/Signin.php";

                    /* Creating a new instance of Sign in Class */
                    SignIn signIn = new SignIn();
                    signIn.execute(urlService, mJSON.toString());
                }
            }
        });

    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class SignIn extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "GET";
        JSONObject mJSON = new JSONObject();

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            String result = "";

            try {
                /* Create a URL object holding our url */
                URL url = new URL(UrlString);
                /* Create an HTTP Connection and adjust its options */
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("Json", "UTF-8") + "=" + URLEncoder.encode(JSONString, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                /* A Stream object to get the returned data from API Call */
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            }
            /* Handling Exceptions */ catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                /* Creating a JSON Object to parse the data in */
                final JSONObject jsonObject = new JSONObject(result);

                /* Creating an Alert Dialog to Show Login Results to User */
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Login to GeeksReads");
                dialog.setMessage(jsonObject.getString("ReturnMsg"));

                sForTest = jsonObject.getString("ReturnMsg");

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (jsonObject.getString("ReturnMsg").contains("Login Succeeded")) {
                                final EditText Email = findViewById((R.id.EmailTxt));
                                final EditText Password = findViewById(R.id.PasswordTxt);
                                Email.setText("");
                                Password.setText("");

                                /* Storing returned Token and User ID */
                                sCurrentToken = jsonObject.getString("ReturnToken");
                                sCurrentUserID = jsonObject.getString("UserID");

                                /* Go to Next Activity Layout */
                                Intent myIntent = new Intent(LoginActivity.this, SideBarActivity.class);
                                startActivity(myIntent);
                            } else {
                                /* If Login didn't succeed, Stay Here in the same Activity and Do Nothing */
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                dialog.show();

            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
