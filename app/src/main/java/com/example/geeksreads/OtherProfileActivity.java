package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class OtherProfileActivity extends AppCompatActivity
{

    ImageView OtherUserPhoto;
    Context mContext;
    TextView UserName;
    TextView BooksCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile);
        mContext=this;



        OtherUserPhoto = findViewById(R.id.UserProfilePhoto);
        UserName = findViewById(R.id.OtherUserName);
        BooksCount = findViewById(R.id.OtherNumberBooks);

        //In my code here, I am not sending any data to the backend:
        JSONObject JSON = new JSONObject();

        System.out.println("1- Created JSON FILE");
        String UrlService = "http://geeksreads.000webhostapp.com/Amr/OtherUserProfile.php";
        OtherProfileActivity.GetOtherProfileDetails MyProfile = new OtherProfileActivity.GetOtherProfileDetails();
        MyProfile.execute(UrlService,JSON.toString());


    }


    private class GetOtherUserPicture extends AsyncTask<String, Void, Bitmap> {

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
            OtherUserPhoto.setImageBitmap(result);

        }
    }


    /**
     * Class that get the data from host and Add it to its views.
     *  The Parameters are host Url and toSend Data.
     */
    public class GetOtherProfileDetails extends AsyncTask<String, Void, String> {
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
            System.out.println("URL: "+UrlString);
            System.out.println("JSON: "+JSONString);
            String result= "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);

                //Create a new InputStreamReader
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
                OtherProfileActivity.GetOtherUserPicture Pic = new OtherProfileActivity.GetOtherUserPicture();
                Pic.execute(jsonObject.getString("photourl"));

                UserName.setText(jsonObject.getString("UserNameData"));

                BooksCount.setText(jsonObject.getString("CountBooks") + " " + "Books");


            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }


}

