package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

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

public class ChangePasswordActivity extends AppCompatActivity {

    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest;
    /**
     * Global Variables to Store Context of this Activity itself
     */
    private Context mContext;

    /* Enum which specifies the kinds of errors that might come from verifyChangePassword function */
    enum changePasswordErrors
    {
        NO_ERRORS,
        OLD_PASSWORD_EMPTY,
        NEW_PASSWORD_LESS_THAN_SIX_CHARS,
        NEW_PASSWORD_HAS_NO_NUMBERS,
        NEW_PASSWORD_HAS_NO_LOWERCASE,
        NEW_PASSWORD_HAS_NO_UPPERCASE,
        NEW_PASSWORD_DONT_MATCH,
        NEW_PASSWORD_EQUAL_OLD
    }
    /* Function to check the input old password and new password to verify the validity of them */
    public static changePasswordErrors verifyChangePassword(String oldPasswordStr, String newPasswordStr, String confNewPasswordStr)
    {
        if (oldPasswordStr.isEmpty()) {
            return changePasswordErrors.OLD_PASSWORD_EMPTY;
        } else if (newPasswordStr.length() < 6) {
            return changePasswordErrors.NEW_PASSWORD_LESS_THAN_SIX_CHARS;
        } else if (!newPasswordStr.matches(".*[0-9].*")) {
            return changePasswordErrors.NEW_PASSWORD_HAS_NO_NUMBERS;
        } else if (!newPasswordStr.matches(".*[a-z].*")) {
            return changePasswordErrors.NEW_PASSWORD_HAS_NO_LOWERCASE;
        } else if (!newPasswordStr.matches(".*[A-Z].*")) {
            return changePasswordErrors.NEW_PASSWORD_HAS_NO_UPPERCASE;
        } else if (!newPasswordStr.equals(confNewPasswordStr)) {
            return changePasswordErrors.NEW_PASSWORD_DONT_MATCH;
        } else if (oldPasswordStr.equals(newPasswordStr)) {
            return changePasswordErrors.NEW_PASSWORD_EQUAL_OLD;
        } else {
            return changePasswordErrors.NO_ERRORS;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        Button saveNewPassword = findViewById(R.id.SaveChangesBtn);
        Button cancel = findViewById(R.id.CancelBtn);

        final EditText oldPasswordTxt = findViewById(R.id.OldPasswordTxt);
        final EditText newPasswordTxt = findViewById(R.id.NewPasswordTxt);
        final EditText confNewPasswordTxt = findViewById(R.id.ConfirmNewPasswordTxt);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPasswordStr = oldPasswordTxt.getText().toString();
                String newPasswordStr = newPasswordTxt.getText().toString();
                String confNewPasswordStr = confNewPasswordTxt.getText().toString();

                changePasswordErrors changePasswordError = verifyChangePassword(oldPasswordStr, newPasswordStr, confNewPasswordStr);
                switch(changePasswordError)
                {
                    case OLD_PASSWORD_EMPTY:
                        oldPasswordTxt.setError("Please enter your old password");
                        sForTest = "Please enter your old password";
                        break;
                    case NEW_PASSWORD_LESS_THAN_SIX_CHARS:
                        newPasswordTxt.setError("Password should be 6 characters or more");
                        newPasswordTxt.setText("");
                        confNewPasswordTxt.setText("");
                        sForTest = "Password should be 6 characters or more";
                        break;
                    case NEW_PASSWORD_HAS_NO_NUMBERS:
                        newPasswordTxt.setError("Password should contain numbers");
                        newPasswordTxt.setText("");
                        confNewPasswordTxt.setText("");
                        sForTest = "Password should contain numbers";
                        break;
                    case NEW_PASSWORD_HAS_NO_LOWERCASE:
                        newPasswordTxt.setError("Password should contain lower case letters");
                        newPasswordTxt.setText("");
                        confNewPasswordTxt.setText("");
                        sForTest = "Password should contain lower case letters";
                        break;
                    case NEW_PASSWORD_HAS_NO_UPPERCASE:
                        newPasswordTxt.setError("Password should contain upper case letters");
                        newPasswordTxt.setText("");
                        confNewPasswordTxt.setText("");
                        sForTest = "Password should contain upper case letters";
                        break;
                    case NEW_PASSWORD_DONT_MATCH:
                        confNewPasswordTxt.setError("Passwords don't match");
                        newPasswordTxt.setText("");
                        confNewPasswordTxt.setText("");
                        sForTest = "Passwords don't match";
                        break;
                    case NEW_PASSWORD_EQUAL_OLD:
                        newPasswordTxt.setError("New password cannot be the same old password");
                        sForTest = "New password cannot be the same old password";
                        break;
                    case NO_ERRORS:
                    default:
                        JSONObject mJSON = new JSONObject();
                        try {

                            /* Encrypting Old Password into MD5 */
                            oldPasswordStr = HelpingFunctions.getMD5Encryption(oldPasswordStr);

                            /* Encrypting New Password into MD5 */
                            newPasswordStr = HelpingFunctions.getMD5Encryption(newPasswordStr);

                            /* Adding Necessary User Data into JSON Object that will be sent */
                            mJSON.put("token", UserSessionManager.getUserToken());
                            mJSON.put("OldUserPassword", oldPasswordStr);
                            mJSON.put("NewUserPassword", newPasswordStr);
                            Log.w("Mahmoud0", mJSON.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /* URL For Change Password API */
                        String urlService = APIs.API_UPDATE_USER_PASSWORD;

                        /* Creating a new instance of Sign in Class */
                        ChangePassword changePassword = new ChangePassword();
                        changePassword.execute(urlService, mJSON.toString());
                        break;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSetting);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(800);
        searchView.setQueryHint("Search books");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    public class ChangePassword extends AsyncTask<String, Void, String> {
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
                String data = JSONString;

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();
                Log.w("Mahmoud1", String.valueOf(http.getResponseCode()));

                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        result = "{\"ReturnMsg\":\"Password changed successfully!\"}";
                        break;
                    default:
                        /* A Stream object to get the returned data from API Call */
                        InputStream ips = http.getErrorStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            result += line;
                        }
                        Log.w("Mahmoud2", result);
                        reader.close();
                        ips.close();
                        if (result.contains("User Doesn't Exist"))
                        {
                            result = "{\"ReturnMsg\":\"An Error Occurred!\"}";
                        }
                        else if (result.contains("Access denied. No token provided."))
                        {
                            result = "{\"ReturnMsg\":\"An Error Occurred!\"}";
                        }
                        else if (result.contains("Invalid Old password."))
                        {
                            result = "{\"ReturnMsg\":\"You entered a wrong old password!\"}";
                        }
                        else
                        {
                            result = "{\"ReturnMsg\":\"An Error Occurred!\"}";
                        }

                        break;
                }

                http.disconnect();
                return result;

            }
            /* Handling Exceptions */
            catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            Log.w("Mahmoud3", result);
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

                if (jsonObject.getString("ReturnMsg").contains("successfully")) {
                    Toast.makeText(mContext, jsonObject.getString("ReturnMsg"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(mContext, jsonObject.getString("ReturnMsg"), Toast.LENGTH_SHORT).show();
                }
            }
            /* Catching Exceptions */ catch (JSONException e) {
                Toast.makeText(mContext, "An Error Occurred!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

}
