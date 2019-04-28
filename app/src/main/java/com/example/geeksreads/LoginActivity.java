package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.net.ContentHandlerFactory;
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
    enum verificationErrorType
    {
        NO_ERRORS,
        INVALID_EMAIL,
        INVALID_PASSWORD
    }
    public static verificationErrorType verifyStaticCredentials(String userEmail, String userPassword)
    {
        /* If the user entered an invalid Email Address */
        if (!userEmail.matches(".+[@].+[.].+")) {
            return verificationErrorType.INVALID_EMAIL;
        }
        /* If the user entered an empty Password */
        else if (userPassword.isEmpty()) {
            return verificationErrorType.INVALID_PASSWORD;
        }
        else {
            return verificationErrorType.NO_ERRORS;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*For Displaying the toolbar on the top of Login Layout  */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        /* Too Act as logged in and re-direct to newsFeed */

       /* SharedPreferences sp;
        sp = getSharedPreferences("login",MODE_PRIVATE);
        sp.edit().putBoolean("logged",true).apply();

        if(sp.getBoolean("logged",false)){
            Intent myIntent = new Intent(LoginActivity.this, FeedActivity.class);
            startActivity(myIntent);
        } */


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
                if (verifyStaticCredentials(loginEmailStr, loginPasswordStr) == verificationErrorType.INVALID_EMAIL) {
                    loginMail.setError("Please enter a valid Email");
                    sForTest = "Please enter a valid Email";
                }
                /* If the user entered an empty Password */
                else if (verifyStaticCredentials(loginEmailStr, loginPasswordStr) == verificationErrorType.INVALID_PASSWORD) {
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
                    String urlService = "https://geeksreads.herokuapp.com/api/auth/signin";

                    /* Creating a new instance of Sign in Class */
                    SignIn signIn = new SignIn();
                    signIn.execute(urlService, mJSON.toString());
                }
            }
        });

        Button forgetPasswordButton = findViewById(R.id.OrForgetBtn);
        /* Function Handler for Clicking on Sign up Button, to Start Checking input Fields
           and Sending JSON String to the Backend Sign up API
         */
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr = loginMail.getText().toString();

                verificationErrorType signUpValidationError = verifyStaticCredentials(emailStr, "sTUBpass123");

                switch (signUpValidationError)
                {
                    case INVALID_EMAIL:
                        loginMail.setError("Please enter a valid Email to Reset your password!");
                        sForTest = "Please enter a valid Email to Reset your password!";
                        break;
                    case NO_ERRORS:
                    default:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setMessage("Are you sure you want to reset password for this email " + emailStr + "?");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject JSON = new JSONObject();
                                try {
                                    JSON.put("UserEmail", emailStr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                /* URL For Forget password API */
                                String urlService = "https://geeksreads.herokuapp.com/api/users/forgetpassword";

                                /* Creating a new instance of Sign up Class */
                                forgetPassword forgetPasswordObj = new forgetPassword();
                                forgetPasswordObj.execute(urlService, JSON.toString());
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* Do Nothing and Stay here in the same layout */
                            }
                        });
                        dialog.show();

                        break;
                }
            }
        });

    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class SignIn extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";
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
                http.setRequestProperty("content-type", "application/json");

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                //String data = URLEncoder.encode(JSONString, "UTF-8");
                String data = JSONString;

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();
                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line = "";
                        //boolean started = false;
                        while ((line = reader.readLine()) != null) {
                            //   if ()
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"Invalid email or password.\"}";
                        break;
                    case "401":
                        result = "{\"ReturnMsg\":\"Your account has not been verified.\"}";
                        break;
                    default:
                        break;
                }


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
                            if (jsonObject.getString("ReturnMsg").contains("Successful")) {
                                final EditText Email = findViewById((R.id.EmailTxt));
                                final EditText Password = findViewById(R.id.PasswordTxt);
                                Email.setText("");
                                Password.setText("");

                                /* Storing returned Token and User ID */
                                sCurrentToken = jsonObject.getString("Token");
                                sCurrentUserID = jsonObject.getString("UserId");

                                /* Go to Next Activity Layout */
                                Intent myIntent = new Intent(LoginActivity.this, FeedActivity.class);
                                startActivity(myIntent);
                            } else {
                                /* If Login didn't succeed, Stay Here in the same Activity and Do Nothing */
                                //Toast.makeText(mContext, "You have entered a wrong username or password!", Toast.LENGTH_SHORT).show();
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

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class forgetPassword extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";

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
                http.setRequestProperty("content-type", "application/json");
                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = JSONString;

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        result = "{\"ReturnMsg\":\"A verification code has been sent to your email address!\"}";
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"This email is not registered before!.\"}";
                        break;
                    default:
                        break;
                }

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

                /* Creating an Alert Dialog to Show Sign up Results to User */
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("Reset Password");
                dialog.setMessage(jsonObject.getString("ReturnMsg"));
                sForTest = jsonObject.getString("ReturnMsg");

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (jsonObject.getString("ReturnMsg").contains("verification")) {
                                /* Go to Next Activity Layout */
                                Intent myIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                                startActivity(myIntent);
                            } else {
                                /* If forget password didn't succeed, Stay Here in the same Activity and Do Nothing */
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
