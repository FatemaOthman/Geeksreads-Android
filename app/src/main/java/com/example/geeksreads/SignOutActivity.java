package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geeksreads.views.LoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import CustomFunctions.UserSessionManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import CustomFunctions.APIs;
/**
 * @author Mahmoud MORSY,
 * This Class Activity handles SignOut Process and Display for Users
 */
public class SignOutActivity extends AppCompatActivity {
    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest;
    /**
     * Global Variables to Store Context of this Activity itself
     */
    public static Context mContext;

    /**
     * Global Variable for LoadingView to be displayed while loading a content from server
     */
    LoadingView Loading;

    /**
     * Starts and Created Signout Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);
        Button loginButton = findViewById(R.id.loginBtn);
        Button signupButton = findViewById(R.id.signupBtn);
        mContext = this;

        TextView allControls[] = {loginButton, signupButton};
        Loading = new LoadingView(allControls, (FrameLayout)findViewById(R.id.progressBarHolder), (TextView)findViewById(R.id.ProgressName));

        /* URL For Sign out API */
        String urlService = APIs.API_SIGNOUT;

        /* Creating a new instance of Sign out Class */
        signOut signOutObject = new signOut();
        signOutObject.execute(urlService, UserSessionManager.getUserToken());

        /* Sign up button command */
        signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //UserSessionManager.resetUserData();
                Intent myIntent = new Intent(SignOutActivity.this, SignupActivity.class);
                myIntent.putExtra("FROM", "SIGNOUT");
                startActivity(myIntent);
            }
        });

        /* Sign in button command */
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SignOutActivity.this, LoginActivity.class);
                myIntent.putExtra("FROM", "SIGNOUT");
                startActivity(myIntent);
            }
        });

        /* Delete all user's data (id and token) */
        UserSessionManager.logOutUser();
    }

    /**
     * @author Mahmoud MORSY,
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class signOut extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";

        /**
         * Function to be done before Executing, it starts Loading Animation
         */
        @Override
        protected void onPreExecute() {
            Loading.Start("Signing out, Please wait...");
        }

        /**
         * Function that executes the logic needed in the background thread
         */
        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String UserToken = params[1];
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

                JSONObject mJSON = new JSONObject();
                mJSON.put("token", UserToken);
                writer.write(mJSON.toString());
                writer.flush();
                writer.close();
                ops.close();

                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        result = "{\"ReturnMsg\":\"Signed out Successfully\"}";
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"Invalid token.\"}";
                        break;
                    case "401":
                        result = "{\"ReturnMsg\":\"Access denied. No token provided.\"}";
                        break;
                    case "402":
                        result = "{\"ReturnMsg\":\"User doesn't exist.\"}";
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
            } catch (JSONException e) {
                e.printStackTrace();
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
                sForTest = jsonObject.getString("ReturnMsg");
            }
            /* Catching Exceptions */
            catch (JSONException e) {
                e.printStackTrace();
            }
            Loading.Stop();
        }
    }

}
