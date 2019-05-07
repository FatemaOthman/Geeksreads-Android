package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import CustomFunctions.APIs;

public class Comments extends AppCompatActivity {

    ListView CommentsList;
    ArrayList<CommentDataModel> dataModels;
    Context mContext;

    EditText CommentTextHolder;
    Button AddCommentBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        mContext = this;
        dataModels = new ArrayList<>();
        CommentsList = findViewById(R.id.CommentsList);

        CommentTextHolder = findViewById(R.id.CommentText);
        AddCommentBtn = findViewById(R.id.AddComment);

        final JSONObject AddCommentJson = new JSONObject();

        final String AddCommentUrl = APIs.API_ADD_COMMENT;

        AddCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!CommentTextHolder.getText().toString().equals("")) {

                    try {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String CurrentDate = sdf.format(System.currentTimeMillis());
                        AddCommentJson.put("UserID", LoginActivity.sCurrentUserID);
                        AddCommentJson.put("reviewId", getIntent().getStringExtra("ReviewId"));
                        AddCommentJson.put("date", CurrentDate);
                        AddCommentJson.put("body", CommentTextHolder.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Comments.AddComment AddTheComment = new Comments.AddComment();
                    AddTheComment.execute(AddCommentUrl, AddCommentJson.toString());

                } else {
                    //Do Nothing.
                }


            }
        });


        // final String CommentsListUrl = "https://geeksreads.herokuapp.com/api/comments/list?ReviewId="+"5ccdb0f8a375f9643cc8b0c3";
        final String CommentsListUrl = APIs.API_GET_COMMENTS_LIST + "?ReviewId=" + getIntent().getStringExtra("ReviewId");

        Comments.GetAllComments performBackgroundTask = new Comments.GetAllComments();
        performBackgroundTask.execute(CommentsListUrl);


    }

    @SuppressLint("StaticFieldLeak")
    public class GetAllComments extends AsyncTask<String, Void, String> {
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

            //  UrlString = UrlString + "?Json=" + bookID;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(UrlString);
            HttpResponse response = null;
            String server_response = null;
            try {
                response = httpclient.execute(httpget);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                dataModels = CommentDataModel.fromJson(jsonArr);
                final CommentCustomAdapter CommentAdapter = new CommentCustomAdapter(dataModels, mContext);
                CommentsList.setAdapter(CommentAdapter);
                CommentsList.deferNotifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class AddComment extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        boolean TaskSucc = false;
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
                http.setRequestProperty("content-type", "application/json");
                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                writer.write(JSONString);
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
                        TaskSucc = true;
                        break;
                    default:
                        Log.d("Test", "String.valueOf(http.getResponseCode()): " + String.valueOf(http.getResponseCode()));
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

            if (!TaskSucc) {
                Toast.makeText(getApplicationContext(), "Unable to Add Comment!", Toast.LENGTH_SHORT).show();
            }
            // Comments.GetAllComments performBackgroundTask = new Comments.GetAllComments();
            // performBackgroundTask.execute(CommentsListUrl);
        }

    }

}
