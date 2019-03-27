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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    public class GetProfileData extends AsyncTask<String, Void, String> {
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
            }
            /* Catching Exceptions */
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

}
