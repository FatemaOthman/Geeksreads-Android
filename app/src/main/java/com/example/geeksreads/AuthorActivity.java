package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import java.util.ArrayList;
import java.util.List;

public class AuthorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<BookItem> list;
    ImageView AuthorPhoto;
    Context mContext;
    TextView AuthorName;
    TextView NumOfBooks;
    TextView AuthorRating;
    RatingBar AuthorRatingBar;
    TextView AuthorDescription;
    TextView AuthorNumsOfRating;
    String ImageURL;
    Button followingState;
    boolean Follow=true;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContext = this;

        AuthorName = findViewById(R.id.AuthorName);
        NumOfBooks = findViewById(R.id.NumberOfBooks);
        AuthorRating = findViewById(R.id.AuthorRating);
        AuthorRatingBar = findViewById(R.id.AuthorRatingBar);
        AuthorDescription = findViewById(R.id.AuthorDesc);
        AuthorPhoto = findViewById(R.id.AuthorPhoto);
        AuthorNumsOfRating=findViewById(R.id.NumsOfRatingAuthor);
        followingState=findViewById(R.id.Follow);
        followingState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code here ...
                if(Follow==true) {
                    followingState.setText("Follow");
                    Follow=false;
                }
                else
                {
                    followingState.setText("Following");
                    Follow=true;
                }

            }
        });

        list = new ArrayList<>();
        JSONObject JSON = new JSONObject();
       try {
            JSON.put("id", "value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String UrlService = "http://geeksreads.000webhostapp.com/Fatema/GetAuthorInfo.php";

        GetAuthorDetails getAuthorDetails = new GetAuthorDetails();
        getAuthorDetails.execute(UrlService, JSON.toString());



    }


    @SuppressLint("StaticFieldLeak")
    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        protected void onPreExecute() {
            //mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String photoUrl = params[0];
                URL url = new URL(photoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            AuthorPhoto.setImageBitmap(result);

        }
    }

    /**
     * Class that get the data from host and Add it to its views.
     * The Parameters are host Url and toSend Data.
     */
    @SuppressLint("StaticFieldLeak")
    private class GetAuthorDetails extends AsyncTask<String, Void, String> {
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

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line;
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
            // mProgressBar.setVisibility(View.GONE);
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage("Done");
                //dialog.show();

                /* Get Json Object from server and preview results on Layout views */
                JSONObject jsonObject = new JSONObject(result);

                AuthorName.setText(jsonObject.getString("name"));
                AuthorNumsOfRating.setText(jsonObject.getString("numofrates")+"  ratings.");

                AuthorRating.setText(jsonObject.getString("rate"));
                AuthorRatingBar.setRating(Float.parseFloat((String)jsonObject.getString("rate")));
                AuthorDescription.setText(jsonObject.getString("disc"));


                if(jsonObject.getString("followed").equals("true"))
                {
                    followingState.setText("Followed");
                    Follow=true;
                }
                else {
                    followingState.setText("Follow");
                    Follow=false;
                }

                /* Start Async Task to get the image from url */
                GetImage getAuthorPic = new GetImage();
                ImageURL = jsonObject.getString("authorpicurl");
                getAuthorPic.execute(ImageURL);
                JSONArray array=jsonObject.getJSONArray("authorbooks");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    BookItem B =new BookItem(
                            o.getString("name"),
                            o.getString("author"),
                            o.getString("bookrate"),
                            o.getString("numofrates"),
                            o.getString("coverurl"),
                            "Read"//o.getString("readingstate")
                    );
                    list.add(B);
                }
                adapter =new BookAdapter(list,getApplicationContext());
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
