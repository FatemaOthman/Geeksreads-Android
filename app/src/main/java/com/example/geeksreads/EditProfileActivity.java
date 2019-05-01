package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import CustomFunctions.UserSessionManager;
import java.text.SimpleDateFormat;
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
import android.widget.ImageView;
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
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import CustomFunctions.APIs;

public class EditProfileActivity extends AppCompatActivity {

    /**
     * Global Public Static Variables used for Testing
     */
    public static String sForTest;

    /**
     * Global Public Static Variables used for Testing Email Verification Step
     */
    public static String sForTest_OriginalEmail;

    /**
     * Global Variables to Store Context of this Activity itself
     */
    private Context mContext;

    /* Function to check the validity of the input date string with a format of DD/MM/YYYY and before 5 Years*/
    public static boolean isThisDateValid(String dateToValidate) {


        Log.w("DATTEEEE", dateToValidate);
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
        sdf.setLenient(false);
        Log.w("DATTEEEE", dateToValidate);
        try {
            // if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            //System.out.println(dateToValidate + " Parsing OK!");
            // current date minus 5 years
            Calendar currentDateBefore5Years = Calendar.getInstance();
            currentDateBefore5Years.add(Calendar.YEAR, -5);
            //System.out.println(dateToValidate + " Subtracting OK!");
            //System.out.println("Original Date = " + dateToValidate + ", Subtracted Date = " + currentDateBefore5Years.getTime().toString());
            if (date.after(currentDateBefore5Years.getTime())) {
                //System.out.println("Original Date = " + dateToValidate + ", Subtracted Date = " + currentDateBefore5Years.getTime().toString() + ",  False");
                Log.w("DATTEEEE", "FALSE");
                return false;
            } else {
                Log.w("DATTEEEE", "TRUE");
                //System.out.println("Original Date = " + dateToValidate + ", Subtracted Date = " + currentDateBefore5Years.getTime().toString() + ",  True");
                return true;
            }

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }
    }

    enum editProfileValidationErrors
    {
        INVALID_USERNAME_LENGTH,
        INVALID_EMAIL,
        INVALID_BIRTH_DATE,
        NO_ERRORS
    }

    /* Function to check the validity of inputs to Edit User API */
    public static editProfileValidationErrors validateEditProfileData(String userNameStr, String emailStr, String birthDateStr)
    {
        /* If the user entered an invalid Username */
        if (userNameStr.length() < 3 || userNameStr.length() > 50) {
            return editProfileValidationErrors.INVALID_USERNAME_LENGTH;
        }
        /* If the user entered an invalid Email Address */
        else if (!emailStr.matches(".+[@].+[.].+")) {
            return editProfileValidationErrors.INVALID_EMAIL;
        }
        else if (!isThisDateValid(birthDateStr)) {
            return editProfileValidationErrors.INVALID_BIRTH_DATE;
        }
        /* If the user entered a valid username, email and password */
        else {
            return editProfileValidationErrors.NO_ERRORS;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContext = this;
        /* URL For Get Current User Info API */
        String urlService = APIs.API_GET_USER_INFO;

        /* Creating a new instance of Sign in Class */
        GetProfileData getProfileData = new GetProfileData();
        JSONObject getDataJson = new JSONObject();
        try {
            Log.w("MahmoudTOKEN", UserSessionManager.getUserToken());
            getDataJson.put("token", UserSessionManager.getUserToken());
            Log.w("Mahmoud___", getDataJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getProfileData.execute(urlService, getDataJson.toString());

        final EditText userNameTxt = findViewById(R.id.UserNameTxt);
        final EditText birthDateTxt = findViewById(R.id.BirthDate);

        Button changePassword = findViewById(R.id.ChangePasswordBtn);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Go to Next Activity Layout */
                Intent myIntent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(myIntent);
            }
        });

        Button saveChange = findViewById(R.id.SaveChangesBtn);
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameStr = userNameTxt.getText().toString();
                String birthDateStr = birthDateTxt.getText().toString();

                userNameTxt.setError(null);
                birthDateTxt.setError(null);

                editProfileValidationErrors editProfileValidationError = validateEditProfileData(userNameStr, "STUB@STUB.COM", birthDateStr);

                switch(editProfileValidationError)
                {
                    case INVALID_USERNAME_LENGTH:
                        userNameTxt.setError("Username length should be 3 characters minimum and 50 characters maximum!");
                        sForTest = "Username length should be 3 characters minimum and 50 characters maximum!";
                        break;
                    case INVALID_EMAIL:
                        sForTest = "Please enter a valid email!";
                        break;
                    case INVALID_BIRTH_DATE:
                        birthDateTxt.setError("Please enter a valid date!");
                        sForTest = "Please enter a valid date!";
                        break;
                    case NO_ERRORS:
                        JSONObject JSON = new JSONObject();
                        try {
                            JSON.put("token", UserSessionManager.getUserToken());
                            JSON.put("NewUserName", userNameStr);
                            JSON.put("NewUserBirthDate", birthDateStr);
                            //JSON.put("NewUserPhoto", userPhotoUrl); //todo upload photo

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /* URL For Update Current User Data API */
                        String urlService = APIs.API_UPDATE_USER_INFO;

                        /* Creating a new instance of Sign up Class */
                        SaveProfileData saveProfileData = new SaveProfileData();
                        saveProfileData.execute(urlService, JSON.toString());
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
        MenuItem item1 = menu.findItem(R.id.NotificationButton);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent mIntent = new Intent(EditProfileActivity.this, NotificationActivity.class);
                startActivity(mIntent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /*  Class GetUserPicture:
     *      Gets user picture from the url and changes it into bitmap
     *      sets the imageView into the same bitmap
     */
    private class GetPicture extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String photoUrl = params[0];
                URL url = new URL(photoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView userProfilePhoto = findViewById(R.id.UserProfilePhoto);
            userProfilePhoto.setImageBitmap(result);

        }
    }

    private class GetProfileData extends AsyncTask<String, Void, String> {
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
            Log.w("Mahmoud12", JSONString);
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
                writer.write(JSONString);
                writer.flush();
                writer.close();
                ops.close();
                Log.w("Mahmoud12000", String.valueOf(http.getResponseCode()));
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
                        Log.w("Mahmoud13", result);
                        break;
                    default:
                        result = "{\"ReturnMsg\":\"An Error Occurred!\"}";
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
            Log.w("Mahmoud14", result);
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if (result.equals("{\"ReturnMsg\":\"An Error Occurred!\"}"))
                {
                    Toast.makeText(mContext,"An Error Occurred!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    /* Creating a JSON Object to parse the data in */
                    final JSONObject jsonObject = new JSONObject(result);

                    EditText userNameTxt = findViewById(R.id.UserNameTxt);
                    EditText birthDateTxt = findViewById(R.id.BirthDate);

                    userNameTxt.setText(jsonObject.getString("UserName"));
                    String bD = jsonObject.getString("UserBirthDate");
                    /* Remove Time */
                    bD = bD.substring(0, bD.indexOf('T'));
                    /* Take Year */
                    String Year = bD.substring(0, bD.indexOf('-'));
                    /* Remove From First until First Dash */
                    bD = bD.substring(bD.indexOf('-')+1);

                    /* Take Month */
                    String Month = bD.substring(0, bD.indexOf('-'));
                    /* Remove From First until Second Dash */
                    bD = bD.substring(bD.indexOf('-')+1);

                    /* Take Day */
                    String Day = bD;
                    String finalDate = Day + "/" + Month + "/" + Year;
                    Log.w("FINALDATEEE", finalDate);
                    birthDateTxt.setText(finalDate);
                    if (!jsonObject.getString("Photo").equals(""))
                    {
                        GetPicture Pic = new GetPicture();
                        Pic.execute(jsonObject.getString("Photo"));
                    }
                    else
                    {
                        /* Do Nothing */
                    }
                }
            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class SaveProfileData extends AsyncTask<String, Void, String> {
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
                switch (String.valueOf(http.getResponseCode()))
                {
                    case "200":
                        result = "{\"ReturnMsg\":\"Your changes are saved successfully!\"}";
                        break;
                    default:
                        result = "{\"ReturnMsg\":\"An error occurred while saving your changes!\"}";
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                /* Creating a JSON Object to parse the data in */
                final JSONObject jsonObject = new JSONObject(result);
                sForTest = jsonObject.getString("ReturnMsg");

                Toast.makeText(mContext, jsonObject.getString("ReturnMsg"), Toast.LENGTH_SHORT).show();

                finish();

            }
            /* Catching Exceptions */ catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}