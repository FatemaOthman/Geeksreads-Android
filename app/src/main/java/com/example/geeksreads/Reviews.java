package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

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

        BookNameForReview.setText(getIntent().getStringExtra("BookName"));

        final String GetAllReviewsURL = APIs.API_GET_REVIEWS_LIST + "?UserId=" + UserSessionManager.getUserID() + "&" +
                "bookId=" + getIntent().getStringExtra("BookID");

        Reviews.GetAllReviews performBackgroundTask = new Reviews.GetAllReviews();
        performBackgroundTask.execute(GetAllReviewsURL);


        ReviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                final ReviewDataModel dataModel = dataModels.get(position);
                Intent intent = new Intent(Reviews.this, Comments.class);
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



        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            String UrlString = params[0];
            String result = "";
            Log.d("AMR", "You entered the do in background in Reviews!");
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(UrlString);
            HttpResponse response = null;
            String server_response = null;
            try {
                response = httpclient.execute(httpget);
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert response != null;
            if (response.getStatusLine().getStatusCode() == 200) {
                try {
                    server_response = EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Server response", server_response);
            } else {
                Log.d("Server response", "Failed to get server response");
            }

            result = server_response;
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
