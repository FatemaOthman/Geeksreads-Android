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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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


public class OtherProfileActivity extends AppCompatActivity {
    public static String aForTestUserName = "";
    public static String aForTestBooksCount = "";

    ImageView OtherUserPhoto;
    Context mContext;
    TextView UserName;
    TextView BooksCount;
    ListView BookShelf;
    Button FollowButton;
    /**
     * onCreate: Instantiate the OtherUSerProfile with data through sending an HTTP request
     * with the UserId and receiving the response in its format.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile);
        mContext = this;


        OtherUserPhoto = findViewById(R.id.UserProfilePhoto);
        UserName = findViewById(R.id.OtherUserName);
        BooksCount = findViewById(R.id.OtherNumberBooks);
        BookShelf = findViewById(R.id.OtherUserBookList);
        FollowButton = findViewById(R.id.FollowButton);
        //In my code here, I am not sending any data to the backend:
        JSONObject JSON = new JSONObject();
        final JSONObject jsonObject = new JSONObject();
        String ID;
        try {
            ID = getIntent().getStringExtra("UserId");
            JSON.put("UserId", ID);
            //TODO: Remove the following hardcoded ID line when merging.
            ID = "value";
            jsonObject.put("UserId", ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String UrlService = "http://geeksreads.000webhostapp.com/Amr/OtherUserProfile.php";
        OtherProfileActivity.GetOtherProfileDetails MyProfile = new OtherProfileActivity.GetOtherProfileDetails();
        MyProfile.execute(UrlService, JSON.toString());
        /////////////////////////////////////////////////////

        final String SecondUrlService = "http://geeksreads.000webhostapp.com/Shrouk/ReadingList.php";
        OtherProfileActivity.GetOtherProfileBooks TheBooks = new OtherProfileActivity.GetOtherProfileBooks();
        TheBooks.execute(SecondUrlService, jsonObject.toString());
        /////////////////////////////////////////////////////

        FollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement the function that handles the requests for follow/unfollow action.

            }
        });


    }

    /**
     * Class GetOtherUserPicture:
     * Gets the image of the user from the JsonObject and translates it into a bitMap to be used by the ImageView.
     */
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
            } catch (Exception e) {
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
     * The Parameters are host Url and toSend Data.
     */
    public class GetOtherProfileDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            String result = "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("UserId", "UTF-8") + "=" + URLEncoder.encode(JSONString, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();


                /////////////////////////////////////////
                //Create a new InputStreamReader
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

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        /**
         * onPostExecute: Takes the string result and treates it as a json object
         * to set data of:
         *  -User Name
         *  -Books Count
         *  -Profile Picture
         *
         * @param result : The result containing all the passed data from backend.
         */
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage("Done");
                //dialog.show();


                JSONObject jsonObject = new JSONObject(result);

                OtherProfileActivity.GetOtherUserPicture Pic = new OtherProfileActivity.GetOtherUserPicture();
                Pic.execute(jsonObject.getString("photourl"));

                UserName.setText(jsonObject.getString("UserNameData"));
                aForTestUserName = UserName.getText().toString();
                BooksCount.setText(jsonObject.getString("CountBooks") + " " + "Books");
                aForTestBooksCount = BooksCount.getText().toString();

                if (jsonObject.getString("FollowStatus").equals("True"))
                    FollowButton.setText("Un-Follow");
                else
                    FollowButton.setText("Follow");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    /////////////////////////////////////////////////

    /**
     * A Private class that extend Async Task to connect to server in background.
     * It get the Want to Read book lists.
     */
    private class GetOtherProfileBooks extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
            //progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String JSONString = params[1];
            String result = "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(JSONString, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
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

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            //progress.setVisibility(View.GONE);
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage(result);
                //dialog.show();
                ListView wantToReadBookList = findViewById(R.id.OtherUserBookList);
                final BookList_JSONAdapter bookListJsonAdapter = new BookList_JSONAdapter(mContext, new JSONArray(result));
                wantToReadBookList.setAdapter(bookListJsonAdapter);
                wantToReadBookList.deferNotifyDataSetChanged();
                wantToReadBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Intent intent = new Intent(OtherProfileActivity.this, BookActivity.class);
                        intent.putExtra("BookISBN", bookListJsonAdapter.getBookISBN());
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    ///////////////////////////////////////////////////
}

