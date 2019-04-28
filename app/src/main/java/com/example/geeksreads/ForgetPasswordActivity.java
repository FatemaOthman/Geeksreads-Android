package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.HelpingFunctions;

public class ForgetPasswordActivity extends AppCompatActivity {
    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest;
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
        EMPTY_VERIFICATION_CODE,
        NEW_PASSWORD_LESS_THAN_SIX_CHARS,
        NEW_PASSWORD_HAS_NO_NUMBERS,
        NEW_PASSWORD_HAS_NO_LOWERCASE,
        NEW_PASSWORD_HAS_NO_UPPERCASE,
        PASSWORDS_DONT_MATCH
    }
    verificationErrorType verifyForgetPasswordData(String verificationCode, String newPassword, String confNewPassword)
    {
        /* If the user entered an invalid Username */
        if (verificationCode.isEmpty()) {
            return verificationErrorType.EMPTY_VERIFICATION_CODE;
        }
        /* If the user entered an invalid Password */
        else if (newPassword.length() < 6) {
            return verificationErrorType.NEW_PASSWORD_LESS_THAN_SIX_CHARS;
        } else if (!newPassword.matches(".*[0-9].*")) {
            return verificationErrorType.NEW_PASSWORD_HAS_NO_NUMBERS;
        } else if (!newPassword.matches(".*[a-z].*")) {
            return verificationErrorType.NEW_PASSWORD_HAS_NO_LOWERCASE;
        } else if (!newPassword.matches(".*[A-Z].*")) {
            return verificationErrorType.NEW_PASSWORD_HAS_NO_UPPERCASE;
        } else if (!newPassword.equals(confNewPassword)) {
            return verificationErrorType.PASSWORDS_DONT_MATCH;
        }
        /* If the user entered a valid verification code and password */
        else {
            return verificationErrorType.NO_ERRORS;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        /*For Displaying the toolbar on the top of Login Layout  */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Forget Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        /* Getting Text boxes and Buttons from the layout */
        Button savePasswordBtn = findViewById(R.id.submitChangedPassword);
        Button cancelBtn = findViewById(R.id.cancelChangePassword);
        final EditText verificationCode = findViewById(R.id.otpTxt);
        final EditText newPassword = findViewById(R.id.passwordTxt);
        final EditText confNewPassword = findViewById(R.id.confPasswordTxt);

        /* Function Handler for Clicking on Save Changed Password Button, to Start Checking input Field
           and Sending JSON String to the Backend  API
         */
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCodeStr = verificationCode.getText().toString();
                String newPasswordStr = newPassword.getText().toString();
                String confNewPasswordStr = confNewPassword.getText().toString();

                verificationErrorType verificationError = verifyForgetPasswordData(verificationCodeStr, newPasswordStr, confNewPasswordStr);

                switch (verificationError)
                {
                    case EMPTY_VERIFICATION_CODE:
                        verificationCode.setError("Please enter the verification code that has been sent to your email");
                        sForTest = "Please enter the verification code that has been sent to your email";
                        break;
                    case NEW_PASSWORD_LESS_THAN_SIX_CHARS:
                        newPassword.setError("Password should be 6 characters or more");
                        newPassword.setText("");
                        confNewPassword.setText("");
                        sForTest = "Password should be 6 characters or more";
                        break;
                    case NEW_PASSWORD_HAS_NO_NUMBERS:
                        newPassword.setError("Password should contain numbers");
                        newPassword.setText("");
                        confNewPassword.setText("");
                        sForTest = "Password should contain numbers";
                        break;
                    case NEW_PASSWORD_HAS_NO_LOWERCASE:
                        newPassword.setError("Password should contain lower case letters");
                        newPassword.setText("");
                        confNewPassword.setText("");
                        sForTest = "Password should contain lower case letters";
                        break;
                    case NEW_PASSWORD_HAS_NO_UPPERCASE:
                        newPassword.setError("Password should contain upper case letters");
                        newPassword.setText("");
                        confNewPassword.setText("");
                        sForTest = "Password should contain upper case letters";
                        break;
                    case PASSWORDS_DONT_MATCH:
                        confNewPassword.setError("Passwords don't match");
                        newPassword.setText("");
                        confNewPassword.setText("");
                        sForTest = "Passwords don't match";
                        break;
                    case NO_ERRORS:
                    default:
                        JSONObject JSON = new JSONObject();
                        try {
                            /* Encrypting New Password into MD5 Format */
                            newPasswordStr = HelpingFunctions.getMD5Encryption(newPasswordStr);

                            /* Adding API parameters into JSON String */
                            JSON.put("VerificationCode", verificationCodeStr);
                            JSON.put("NewPassword", newPasswordStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /* URL For Forget Password API */
                        String urlService = "https://geeksreads.herokuapp.com/api/users/forgetpassword2";

                        /* Creating a new instance of Sign up Class */
                        forgetPasswordClass forgetPassword = new forgetPasswordClass();
                        forgetPassword.execute(urlService, JSON.toString());
                        break;
                }
            }
        });

        /* Function Handler for Clicking on Save Changed Password Button, to Start Checking input Field
           and Sending JSON String to the Backend  API
         */
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public class forgetPasswordClass extends AsyncTask<String, Void, String> {
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
                        result = "{\"ReturnMsg\":\"Your password is changed successfully\"}";
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"Invalid verification code.\"}";
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
                            if (jsonObject.getString("ReturnMsg").contains("successfully")) {
                                /* Go to Next Activity Layout */
                                Intent myIntent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
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
