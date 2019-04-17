package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class Followers_Fragment extends Fragment {

    ListView FollowersList;
    Context mContext;
    ArrayList<UserDataModel> dataModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followers_view, container, false);
        FollowersList = view.findViewById(R.id.FollowersList);
        ///////////////////////////////////////////////////////////////////////
        mContext = getContext();
        dataModels = new ArrayList<>();
        final String CurrentUser = FollowActivity.getCurrentID();
        //In my code here, I am sending the id of the user
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("UserId", FollowActivity.getCurrentID()); //Value will be passed from FollowActivity.java
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Calling Async Task with my server url
        String UrlService = "http://geeksreads.000webhostapp.com/Amr/GetFollowers.php";
        Followers_Fragment.GetDetails MyFollowers = new GetDetails();
        MyFollowers.execute(UrlService, JSON.toString());


        FollowersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final UserDataModel dataModel = dataModels.get(position);

                Intent myIntent = new Intent(getActivity(), OtherProfileActivity.class);
                myIntent.putExtra("UserId", CurrentUser);
                myIntent.putExtra("FollowId", dataModel.getID());
                startActivity(myIntent);

            }
        });


        return view;
    }

    public class GetDetails extends AsyncTask<String, Void, String> {
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
                //String data = URLEncoder.encode(JSONString, "UTF-8");
                String data = JSONString;

                writer.write(data);
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
                            //   if ()
                            result += line;
                        }
                        reader.close();
                        ips.close();
                        break;
                    case "400":
                        result = "{\"ReturnMsg\":\"Invalid email or password.\"}";
                        break;
                    case "401":
                        result = "{\"ReturnMsg\":\"Your account has not been verified.\"}";
                        break;
                    default:
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
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                JSONArray jsonArr = new JSONArray(result);

                dataModels = UserDataModel.fromJson(jsonArr);
                //Data is sent and passed correctly inside the model.
                CustomAdapter followerAdapter = new CustomAdapter(dataModels, mContext.getApplicationContext());
                FollowersList.setAdapter(followerAdapter);
                followerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
