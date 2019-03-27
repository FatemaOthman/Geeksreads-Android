package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
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

public class EditProfileActivity extends AppCompatActivity {

    /** Global Variables to Store Context of this Activity itself */
    private Context mContext;
    /** Global Public Static Variables used for Testing */
    public static String sForTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Change the below two lines after integration
        LoginActivity.sCurrentUserID = "iiiidddd1142019";
        LoginActivity.sCurrentToken = "xYzAbCdToKeN";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        JSONObject mJSON = new JSONObject();
        try {
            mJSON.put("UserID", LoginActivity.sCurrentUserID);
            mJSON.put("UserToken", LoginActivity.sCurrentToken);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        /* URL For Login API */
        String urlService = "http://geeksreads.000webhostapp.com/Morsy/GetProfileDetails.php";

        /* Creating a new instance of Sign in Class */
        GetProfileData getProfileData = new GetProfileData();
        getProfileData.execute(urlService,mJSON.toString());

        final EditText fullNameTxt = findViewById(R.id.FullNameTxt);
        final EditText userNameTxt = findViewById(R.id.UserNameTxt);
        final EditText emailAddressTxt = findViewById(R.id.EmailAddressTxt);
        final EditText birthDateTxt = findViewById(R.id.BirthDate);
        final Spinner countryList = findViewById(R.id.CountryList);
        final Spinner genderList = findViewById(R.id.GenderList);

        Button changePassword = findViewById(R.id.ChangePasswordBtn);

        changePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                /* Go to Next Activity Layout */
                Intent myIntent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(myIntent);
            }
        });

        Button saveChange = findViewById(R.id.SaveChangesBtn);
        saveChange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fullNameStr = fullNameTxt.getText().toString();
                String emailStr = emailAddressTxt.getText().toString();
                String userNameStr = userNameTxt.getText().toString();
                String birthDateStr = birthDateTxt.getText().toString();
                String countryStr = countryList.getSelectedItem().toString();
                String genderStr = genderList.getSelectedItem().toString();

                fullNameTxt.setError(null);
                emailAddressTxt.setError(null);
                userNameTxt.setError(null);
                birthDateTxt.setError(null);
                /* If the user entered an invalid Username */
                if (userNameStr.length() < 3 || userNameStr.length() > 50)
                {
                    userNameTxt.setError("Username length should be 3 characters minimum and 50 characters maximum");
                    //sForTest = "Username length should be 3 characters minimum and 50 characters maximum";
                }
                /* If the user entered an invalid Email Address */
                else if (!emailStr.matches(".+[@].+[.].+"))
                {
                    emailAddressTxt.setError("Please enter a valid email");
                    //sForTest = "Please enter a valid Email";
                }
                /* If the user entered an invalid Full Name */
                else if (fullNameStr.length() < 5 || fullNameStr.length() > 100)
                {
                    fullNameTxt.setError("Full name should be 5 characters minimum and 100 characters maximum");
                    //sForTest = "Please enter a valid Email";
                }
                else if (!birthDateStr.matches("[0-9]+[/][0-9]+[/][1][9][0-9][0-9]"))
                {
                    birthDateTxt.setError("Please enter a valid date");
                }
                /* If the user entered a valid username, email and password */
                else
                {
                    JSONObject JSON = new JSONObject();
                    try {
                        JSON.put("UserID", LoginActivity.sCurrentUserID);
                        JSON.put("UserToken", LoginActivity.sCurrentToken);
                        JSON.put("FullName", fullNameStr);
                        JSON.put("UserName", userNameStr);
                        JSON.put("EmailAddress", emailStr);
                        JSON.put("Gender", genderStr);
                        JSON.put("Country", countryStr);
                        JSON.put("BirthDate", birthDateStr);

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /* URL For Sign up API */
                    String urlService = "http://geeksreads.000webhostapp.com/Morsy/SetProfileDetails.php";

                    /* Creating a new instance of Sign up Class */
                    SaveProfileData saveProfileData = new SaveProfileData();
                    saveProfileData.execute(urlService,JSON.toString());
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
            }catch (Exception e){

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
        JSONObject mJSON = new JSONObject();
        static final String REQUEST_METHOD="GET";

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

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
                String data = URLEncoder.encode("Json","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                /* A Stream object to get the returned data from API Call */
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            }
            /* Handling Exceptions */
            catch (MalformedURLException e) {
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
                /* Creating a JSON Object to parse the data in */
                final JSONObject jsonObject = new JSONObject(result);

                EditText fullNameTxt = findViewById(R.id.FullNameTxt);
                EditText userNameTxt = findViewById(R.id.UserNameTxt);
                EditText emailAddressTxt = findViewById(R.id.EmailAddressTxt);
                EditText birthDateTxt = findViewById(R.id.BirthDate);
                Spinner countryList = findViewById(R.id.CountryList);
                Spinner genderList = findViewById(R.id.GenderList);

                fullNameTxt.setText(jsonObject.getString("FullName"));
                userNameTxt.setText(jsonObject.getString("UserName"));
                emailAddressTxt.setText(jsonObject.getString("EmailAddress"));
                birthDateTxt.setText(jsonObject.getString("BirthDate"));

                String compareValue = jsonObject.getString("Country");
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.Countries, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countryList.setAdapter(adapter);
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    countryList.setSelection(spinnerPosition);
                }

                compareValue = jsonObject.getString("Gender");
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(mContext, R.array.Gender, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                genderList.setAdapter(adapter2);
                if (compareValue != null) {
                    int spinnerPosition = adapter.getPosition(compareValue);
                    genderList.setSelection(spinnerPosition);
                }

                GetPicture Pic = new GetPicture();
                Pic.execute(jsonObject.getString("PhotoUrl"));
            }
            /* Catching Exceptions */
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

    private class SaveProfileData extends AsyncTask<String, Void, String> {
        JSONObject mJSON = new JSONObject();
        static final String REQUEST_METHOD="GET";

        @Override
        protected void onPreExecute() {
            /* Do Nothing */
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

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
                String data = URLEncoder.encode("Json","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                /* A Stream object to get the returned data from API Call */
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            }
            /* Handling Exceptions */
            catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                /* Creating a JSON Object to parse the data in */
                final JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("ReturnMsg").equals("Your changes are saved successfully"))
                {
                    Toast.makeText(mContext,"Your changes are saved successfully", Toast.LENGTH_SHORT).show();
                }
                else if (jsonObject.getString("ReturnMsg").contains("A verification email has been sent"))
                {
                    dialog.setMessage(jsonObject.getString("ReturnMsg"));
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            /* Go to Next Activity Layout */
                            Intent myIntent = new Intent(EditProfileActivity.this, MyBooksShelvesActivity.class);
                            startActivity(myIntent);
                        }
                    });
                    dialog.show();
                }
                else
                {
                    Toast.makeText(mContext,"An Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
            /* Catching Exceptions */
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

}
