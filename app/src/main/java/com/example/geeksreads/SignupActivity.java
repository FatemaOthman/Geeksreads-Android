package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.geeksreads.views.LoadingView;
import org.json.JSONException;
import org.json.JSONObject;
import CustomFunctions.UserSessionManager;
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
import java.nio.charset.StandardCharsets;
import CustomFunctions.HelpingFunctions;
import CustomFunctions.APIs;

/**
 * @author Mahmoud MORSY,
 * This Activity class is for Sign up and Creating a new account for new users in Geekreads website
 */
public class SignupActivity extends AppCompatActivity {
    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest;
    /**
     * Global Private Variable to Store Sign up Username from Text boxes
     */
    private String fullNameStr;
    /**
     * Global Private Variable to Store Sign up Email Address from Text boxes
     */
    private String emailStr;
    /**
     * Global Private Variable to Store Sign up Password from Text boxes
     */
    private String passwordStr;
    /**
     * Global Variables to Store Context of this Activity itself
     */
    private Context mContext;

    /**
     * Global Variable to Use Loading Animation while fetching or sending data to server
     */
    LoadingView Loading;
    /**
     * Enum for all types of validation errors in sign up data
     */
    enum signUpValidationErrors
    {
        INVALID_USERNAME_LENGTH,
        INVALID_EMAIL,
        NEW_PASSWORD_LESS_THAN_SIX_CHARS,
        NEW_PASSWORD_HAS_NO_NUMBERS,
        NEW_PASSWORD_HAS_NO_LOWERCASE,
        NEW_PASSWORD_HAS_NO_UPPERCASE,
        NEW_PASSWORD_DONT_MATCH,
        NO_ERRORS,
    }

    /**
     * Function to check the validity of Data sent to Sign Up API
     * @param fullNameStr UserName of the New User
     * @param emailStr Email Address of the New User
     * @param passwordStr Password of the New User
     * @param confPasswordStr Confirmation Password of the New User
     * @return Sign Up Validation Error from ENUM defined
     */
    public static signUpValidationErrors validateSignUpData(String fullNameStr, String emailStr, String passwordStr, String confPasswordStr)
    {
        /* If the user entered an invalid Username */
        if (fullNameStr.length() < 3 || fullNameStr.length() > 50) {
            return signUpValidationErrors.INVALID_USERNAME_LENGTH;
        }
        /* If the user entered an invalid Email Address */
        else if (!emailStr.matches(".+[@].+[.].+")) {
            return signUpValidationErrors.INVALID_EMAIL;
        }
        /* If the user entered an invalid Password */
        else if (passwordStr.length() < 6) {
            return signUpValidationErrors.NEW_PASSWORD_LESS_THAN_SIX_CHARS;
        } else if (!passwordStr.matches(".*[0-9].*")) {
            return signUpValidationErrors.NEW_PASSWORD_HAS_NO_NUMBERS;
        } else if (!passwordStr.matches(".*[a-z].*")) {
            return signUpValidationErrors.NEW_PASSWORD_HAS_NO_LOWERCASE;
        } else if (!passwordStr.matches(".*[A-Z].*")) {
            return signUpValidationErrors.NEW_PASSWORD_HAS_NO_UPPERCASE;
        } else if (!passwordStr.equals(confPasswordStr)) {
            return signUpValidationErrors.NEW_PASSWORD_DONT_MATCH;
        }
        /* If the user entered a valid username, email and password */
        else {
            return signUpValidationErrors.NO_ERRORS;
        }
    }

    /**
     * Override function for custom navigation for back button
     */
    @Override
    public void onBackPressed()
    {
        if (!getIntent().getStringExtra("FROM").equals("SIGNOUT"))
        {
            this.moveTaskToBack(true);
        }
        else
        {
            super.onBackPressed();
        }
    }
    /**
     * Function for Starting Logic Actions after Creating the Layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* If user is already logged in, Skip and go to Main Activity */
        mContext=this;
        if (!APIs.TestingModeEnabled)
        {
            UserSessionManager.Initialize(mContext);
            if (!getIntent().getStringExtra("FROM").equals("SIGNIN") && !getIntent().getStringExtra("FROM").equals("SIGNOUT")) {
                UserSessionManager.UserSessionState currentUserState=UserSessionManager.getCurrentState();
                if (currentUserState == UserSessionManager.UserSessionState.USER_LOGGED_IN) {
                    /* Go to Next Activity Layout */
                    Intent myIntent=new Intent(SignupActivity.this, FeedActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                } else if (currentUserState == UserSessionManager.UserSessionState.USER_DATA_AVAILABLE_BUT_NOT_LOGGED_IN) {
                    /* Go to Next Activity Layout */
                    Intent myIntent=new Intent(SignupActivity.this, LoginActivity.class);
                    myIntent.putExtra("FROM", "SIGNUP");
                    startActivity(myIntent);
                } else {
                    /* Stay Here */
                }
            } else {
                /* Stay Here */
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button loginLink = findViewById(R.id.OrLoginLinkBtn);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
                myIntent.putExtra("FROM", "SIGNUP");
                startActivity(myIntent);
            }
        });

        /* Getting Text boxes and Buttons from the layout */
        final EditText fullName = findViewById(R.id.UserNameTxt);
        final EditText email = findViewById((R.id.EmailTxt));
        final EditText password = findViewById(R.id.PasswordTxt);
        final EditText confPassword = findViewById(R.id.ConfirmPasswordTxt);

        Button signUpButton = findViewById(R.id.SignupBtn);
        /* Function Handler for Clicking on Sign up Button, to Start Checking input Fields
           and Sending JSON String to the Backend Sign up API
         */

        TextView allControls[] = {fullName, email, password, confPassword, signUpButton};
        Loading = new LoadingView(allControls, (FrameLayout)findViewById(R.id.progressBarHolder), (TextView)findViewById(R.id.ProgressName));
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullNameStr = fullName.getText().toString();
                emailStr = email.getText().toString();
                passwordStr = password.getText().toString();
                String confPasswordStr = confPassword.getText().toString();

                signUpValidationErrors signUpValidationError = validateSignUpData(fullNameStr, emailStr, passwordStr, confPasswordStr);

                switch (signUpValidationError)
                {
                    case INVALID_USERNAME_LENGTH:
                        fullName.setError("Username length should be 3 characters minimum and 50 characters maximum");
                        sForTest = "Username length should be 3 characters minimum and 50 characters maximum";
                        break;
                    case INVALID_EMAIL:
                        email.setError("Please enter a valid Email");
                        sForTest = "Please enter a valid Email";
                        break;
                    case NEW_PASSWORD_LESS_THAN_SIX_CHARS:
                        password.setError("Password should be 6 characters or more");
                        password.setText("");
                        confPassword.setText("");
                        sForTest = "Password should be 6 characters or more";
                        break;
                    case NEW_PASSWORD_HAS_NO_NUMBERS:
                        password.setError("Password should contain numbers");
                        password.setText("");
                        confPassword.setText("");
                        sForTest = "Password should contain numbers";
                        break;
                    case NEW_PASSWORD_HAS_NO_LOWERCASE:
                        password.setError("Password should contain lower case letters");
                        password.setText("");
                        confPassword.setText("");
                        sForTest = "Password should contain lower case letters";
                        break;
                    case NEW_PASSWORD_HAS_NO_UPPERCASE:
                        password.setError("Password should contain upper case letters");
                        password.setText("");
                        confPassword.setText("");
                        sForTest = "Password should contain upper case letters";
                        break;
                    case NEW_PASSWORD_DONT_MATCH:
                        confPassword.setError("Passwords don't match");
                        password.setText("");
                        confPassword.setText("");
                        sForTest = "Passwords don't match";
                        break;
                    case NO_ERRORS:
                    default:
                        JSONObject JSON = new JSONObject();
                        try {
                            /* Encrypting User Password */
                            passwordStr = HelpingFunctions.getMD5Encryption(passwordStr);

                            /* Adding API Parameters into JSON Object */
                            JSON.put("UserName", fullNameStr);
                            JSON.put("UserEmail", emailStr);
                            JSON.put("UserPassword", passwordStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /* URL For Sign up API */
                        String urlService = APIs.API_SIGNUP;

                        /* Creating a new instance of Sign up Class */
                        signUp signUp = new signUp();
                        signUp.execute(urlService, JSON.toString());
                        break;
                }
            }
        });


    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class signUp extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "POST";

        /**
         * PreExecute function that starts loading animation
         */
        @Override
        protected void onPreExecute() {
            Loading.Start("Creating account, Please wait...");
        }

        /**
         * Function that executes signup logic and send data to server in background
         * @return Resulting response from server
         */
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
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line = "";
                        //boolean started = false;
                        while ((line = reader.readLine()) != null) {
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    case "400":
                        /* A Stream object to get the returned data from API Call */
                        InputStream Errips = http.getErrorStream();
                        BufferedReader Errreader = new BufferedReader(new InputStreamReader(Errips, StandardCharsets.ISO_8859_1));
                        String Errline = "";
                        //boolean started = false;
                        while ((Errline = Errreader.readLine()) != null) {
                            result += Errline;
                        }
                        Errreader.close();
                        Errips.close();
                        if (result.contains("registered"))
                        {
                            result = "{\"ReturnMsg\":\"User already registered.\"}";
                        }
                        else
                        {
                            result = "{\"ReturnMsg\":\"An error occurred!\"}";
                        }
                        break;
                    default:
                        break;
                }

                /* A Stream object to get the returned data from API Call */
                //http.getResponseMessage();

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

        /**
         * Function that does that changes in layout and take corresponding action for the incoming
         * server response
         * @param result Response taken from server
         */
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
                dialog.setTitle("Create account on GeeksReads");
                dialog.setMessage(jsonObject.getString("ReturnMsg"));
                sForTest = jsonObject.getString("ReturnMsg");

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (jsonObject.getString("ReturnMsg").contains("A verification email has been sent")) {
                                final EditText FullName = findViewById(R.id.UserNameTxt);
                                final EditText Email = findViewById((R.id.EmailTxt));
                                final EditText Password = findViewById(R.id.PasswordTxt);
                                final EditText ConfPassword = findViewById(R.id.ConfirmPasswordTxt);
                                FullName.setText("");
                                Email.setText("");
                                Password.setText("");
                                ConfPassword.setText("");

                                /* Go to Next Activity Layout */
                                Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                myIntent.putExtra("FROM", "SIGNUP");
                                startActivity(myIntent);
                            } else {
                                /* If Sign up didn't succeed, Stay Here in the same Activity and Do Nothing */
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Loading.Stop();
                dialog.show();
            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
            }
            Loading.Stop();
        }

    }
}
