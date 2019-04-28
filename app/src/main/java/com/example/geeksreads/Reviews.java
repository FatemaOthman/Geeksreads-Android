package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Reviews extends AppCompatActivity {

    ListView ReviewList;
    ArrayList<ReviewDataModel> dataModels;
    TextView BookNameForReview;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        mContext = this;
        dataModels = new ArrayList<>();
        ReviewList = findViewById(R.id.ReviewsList);
        BookNameForReview = findViewById(R.id.ReviewsName);
        //TODO: Remove next line and uncomment the following one.
        //Don't forget to check with Sherouk that the BookName is passed.
        BookNameForReview.setText("Pride And Prejudice");
        //    BookNameForReview.setText(getIntent().getStringExtra("BookName"));
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserID", "value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String UrlService = "http://geeksreads.000webhostapp.com/Amr/ReviewList.php";

        Reviews.GetAllReviews performBackgroundTask = new Reviews.GetAllReviews();
        performBackgroundTask.execute(UrlService, jsonObject.toString());


        ReviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final ReviewDataModel dataModel = dataModels.get(position);
                Intent intent;
                intent = new Intent(Reviews.this, Comments.class);
                intent.putExtra("ReviewID", dataModel.getReviewID());
                startActivity(intent);
            }

        });
    }

    @SuppressLint("StaticFieldLeak")
    public class GetAllReviews extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        /**
         * doInBackground: Returns result string through sending and HTTP request and receiving the response.
         *
         * @param params
         * @return result
         */

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
                http.setRequestProperty("x-auth-token", LoginActivity.sCurrentToken);

                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                writer.write("");
                writer.flush();
                writer.close();
                ops.close();

                switch (String.valueOf(http.getResponseCode())) {
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
            return result;
        }


        /////////////////////////////////////////////////////////////////////////////////////////

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                dialog.setMessage(result);
                //dialog.show();

                JSONArray jsonArr = new JSONArray(result);
                dataModels = ReviewDataModel.fromJson(jsonArr);
                final ReviewsCustomAdapter ReviewAdapter = new ReviewsCustomAdapter(dataModels, mContext);
                ReviewList.setAdapter(ReviewAdapter);
                ReviewList.deferNotifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



}
