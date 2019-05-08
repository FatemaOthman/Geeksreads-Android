package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geeksreads.views.LoadingView;

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

import CustomFunctions.APIs;
import CustomFunctions.UserSessionManager;

public class Following_Fragment extends Fragment {

    ListView FollowersList;
    Context mContext;
    ArrayList<UserDataModel> dataModels;
    private CustomAdapter FollowingAdapter;
    LoadingView Loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.following_view, container, false);
        FollowersList = view.findViewById(R.id.FollowingList);
        ///////////////////////////////////////////////////////////////////////
        mContext = getContext();
        dataModels = new ArrayList<>();
        Loading = new LoadingView(null, (FrameLayout)view.findViewById(R.id.progressBarHolder), (TextView)view.findViewById(R.id.ProgressName));
        //In my code here, I am sending the id of the user
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("user_id", UserSessionManager.getUserID());
            JSON.put("token", UserSessionManager.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Calling Async Task with my server url
        String UrlService = APIs.API_GET_FOLLOWING_LIST;
        Following_Fragment.GetDetails MyFollowing = new GetDetails();
        MyFollowing.execute(UrlService, JSON.toString());

        /**
         * send ID of the user I clicked on to the OtherProfile Activity
         */
        FollowersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final UserDataModel dataModel = dataModels.get(position);

                Intent myIntent = new Intent(getActivity(), OtherProfileActivity.class);
                myIntent.putExtra("UserId", UserSessionManager.getUserID());
                myIntent.putExtra("FollowId", dataModel.getID());
                startActivity(myIntent);
            }
        });


        return view;
    }

    public class GetDetails extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            Loading.Start("Loading, Please wait...");
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
                http.setRequestProperty("x-auth-token", UserSessionManager.getUserToken());
                /* A Stream object to hold the sent data to API Call */
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));

                writer.write(JSONString);
                writer.flush();
                writer.close();
                ops.close();
                Log.d("AMR", "Follow :http.getResponseCode(): " + String.valueOf(http.getResponseCode()));
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
        /**
         * onPostExecute: Fill Adapter with User Data Models from the JsonArray.
         * @param result
         */
        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(mContext, "Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Log.d("AMR", "Following: " + result);
                JSONArray jsonArr = new JSONArray(result);
                dataModels = UserDataModel.fromJson(jsonArr);
                FollowingAdapter = new CustomAdapter(dataModels, mContext.getApplicationContext());
                FollowersList.setAdapter(FollowingAdapter);
                FollowingAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Loading.Stop();
        }

    }


}

