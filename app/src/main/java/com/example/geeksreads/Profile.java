package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Profile extends AppCompatActivity
{

    ImageView UserPhoto;
    Context mContext;
    TextView FollowersCount;
    TextView FollowingCount;
    TextView BooksCount;


    public static String ForTestProfilePicture,ForTestFollowersCount, ForTestFollowingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mContext=this;

        //////////////////////////////////////////////////////
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView Followers= findViewById(R.id.ActualFollowersCount);
        Followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this,FollowActivity.class);
                startActivity(myIntent);
            }
        });

        TextView Following= findViewById(R.id.ActualFollowingCount);
        Following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this,FollowActivity.class);
                startActivity(myIntent);
            }
        });


        //////////////////////////////////////////////////////
        //Strings and Variables:

        mContext = this;


        UserPhoto = findViewById(R.id.UserProfilePhoto);
        FollowersCount = Followers;
        FollowingCount = Following;
        BooksCount = findViewById(R.id.NumberOfBooks);

        //In my code here, I am not sending any data to the backend:
        JSONObject JSON = new JSONObject();
        /*
        try {
            // TODO: Put all your JSON values Here.
            JSON.put("Picture", "value");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        */

        // TODO: Change the URL with your Service.
        String UrlService = "http://geeksreads.000webhostapp.com/Amr/UserProfile.php";

        Profile.GetProfileDetails MyProfile = new Profile.GetProfileDetails();
        MyProfile.execute(UrlService,JSON.toString());



    }


    private class GetUserPicture extends AsyncTask<String, Void, Bitmap> {

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
                // Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            UserPhoto.setImageBitmap(result);

        }
    }


    /**
     * Class that get the data from host and Add it to its views.
     *  The Parameters are host Url and toSend Data.
     */
    public class GetProfileDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            } catch (MalformedURLException e) {
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
                dialog.setMessage("Done");
                //dialog.show();

                // TODO: Add your Post Execute logic here.
                JSONObject jsonObject = new JSONObject(result);
                FollowersCount.setText(jsonObject.getString("Followers"));
                FollowingCount.setText(jsonObject.getString("Following"));
                BooksCount.setText(jsonObject.getString("CountBooks") + " " + "Books");
                Profile.GetUserPicture Pic = new Profile.GetUserPicture();
                Pic.execute(jsonObject.getString("photourl"));

                ForTestProfilePicture = jsonObject.getString("photourl");
                ForTestFollowersCount = FollowersCount.getText().toString();
                ForTestFollowingCount = FollowingCount.getText().toString();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }


}
