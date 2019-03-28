package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ChangePasswordActivity extends AppCompatActivity {

    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest;
    /**
     * Global Variables to Store Context of this Activity itself
     */
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO Remove the below two lines after integration
        LoginActivity.sCurrentUserID = "iiiidddd1142019";
        LoginActivity.sCurrentToken = "xYzAbCdToKeN";

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

                if (oldPasswordStr.isEmpty()) {
                    oldPasswordTxt.setError("Please enter your old password");
                } else if (newPasswordStr.length() < 6) {
                    newPasswordTxt.setError("Password should be 6 characters or more");
                    newPasswordTxt.setText("");
                    confNewPasswordTxt.setText("");
                    sForTest = "Password should be 6 characters or more";
                } else if (!newPasswordStr.matches(".*[0-9].*")) {
                    newPasswordTxt.setError("Password should contain numbers");
                    newPasswordTxt.setText("");
                    confNewPasswordTxt.setText("");
                    sForTest = "Password should contain numbers";
                } else if (!newPasswordStr.matches(".*[a-z].*")) {
                    newPasswordTxt.setError("Password should contain lower case letters");
                    newPasswordTxt.setText("");
                    confNewPasswordTxt.setText("");
                    sForTest = "Password should contain lower case letters";
                } else if (!newPasswordStr.matches(".*[A-Z].*")) {
                    newPasswordTxt.setError("Password should contain upper case letters");
                    newPasswordTxt.setText("");
                    confNewPasswordTxt.setText("");
                    sForTest = "Password should contain upper case letters";
                } else if (!newPasswordStr.equals(confNewPasswordStr)) {
                    confNewPasswordTxt.setError("Passwords don't match");
                    newPasswordTxt.setText("");
                    confNewPasswordTxt.setText("");
                    sForTest = "Passwords don't match";
                } else if (oldPasswordStr.equals(newPasswordStr)) {
                    newPasswordTxt.setError("New password cannot be the same old password");
                    sForTest = "New password cannot be the same old password";
                } else {
                    JSONObject mJSON = new JSONObject();
                    try {
                        mJSON.put("UserID", LoginActivity.sCurrentUserID);
                        mJSON.put("UserToken", LoginActivity.sCurrentToken);
                        mJSON.put("UserPassword", oldPasswordStr);
                        mJSON.put("NewPassword", newPasswordStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /* URL For Login API */
                    String urlService = "http://geeksreads.000webhostapp.com/Morsy/ChangePassword.php";

                    /* Creating a new instance of Sign in Class */
                    ChangePassword changePassword = new ChangePassword();
                    changePassword.execute(urlService, mJSON.toString());

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

                sForTest = jsonObject.getString("ReturnMsg");

                if (jsonObject.getString("ReturnMsg").equals("Your new password is saved successfully")) {
                    Toast.makeText(mContext, "Your new password is saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (jsonObject.getString("ReturnMsg").equals("You cannot use the same password again")) {
                    Toast.makeText(mContext, "You cannot use the same password again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
