package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Followers_Fragment extends Fragment {

    ListView FollowersList;
    Context mContext;
    ArrayList<UserDataModel> dataModels;
    private CustomAdapter FollowerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followers_view, container, false);
        FollowersList = view.findViewById(R.id.FollowersList);
        ///////////////////////////////////////////////////////////////////////
        mContext = getContext();
        dataModels = new ArrayList<>();

        //In my code here, I am sending the id of the user
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("UserId", FollowActivity.getCurrentID()); //Value will be passed from FollowActivity.java
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Calling Async Task with my server url
        String UrlService = "http://geeksreads.000webhostapp.com/Amr/GetFollowers.php";
        Log.i("AMR", UrlService);
        Followers_Fragment.GetDetails MyFollowers = new GetDetails();
        MyFollowers.execute(UrlService, JSON.toString());


        FollowersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final UserDataModel dataModel = dataModels.get(position);

                        Intent myIntent = new Intent(getActivity(), OtherProfileActivity.class);
                Log.i("AMR", "SentID: " + dataModel.getID());
                        myIntent.putExtra("UserID", dataModel.getID());
                        startActivity(myIntent);

            }
        });


        return view;
    }

    public class GetDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";

        // AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            //  dialog = new AlertDialog.Builder(mContext).create();
            //   dialog.setTitle("Connection Status");
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
                //Log.i("AMR","RES: "+result);
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
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                JSONArray jsonArr = new JSONArray(result);
                // Log.i("AMR","JSONARR: "+jsonArr);
                dataModels = UserDataModel.fromJson(jsonArr);
                //Data is sent and passed correctly inside the model.
                FollowerAdapter = new CustomAdapter(dataModels, mContext.getApplicationContext());
                FollowersList.setAdapter(FollowerAdapter);
                FollowerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
