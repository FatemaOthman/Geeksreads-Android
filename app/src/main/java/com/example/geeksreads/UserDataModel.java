package com.example.geeksreads;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDataModel {

    String Picture_Url;
    String User_Name;
    String User_ID;


    public static UserDataModel fromJson(JSONObject jsonObject) {
        UserDataModel DummyUser = new UserDataModel();
        // Deserialize json into object fields
        try {
            DummyUser.User_ID = jsonObject.getString("id");
            DummyUser.Picture_Url = jsonObject.getString("photourl");
            DummyUser.User_Name = jsonObject.getString("name");

            Log.i("AMR", "DummyUser: " + DummyUser);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return DummyUser;
    }

    public static ArrayList<UserDataModel> fromJson(JSONArray jsonArray) {
        JSONObject UsersJson;
        ArrayList<UserDataModel> AllUsers = new ArrayList<UserDataModel>(jsonArray.length());
        // Process each result in json array, decode and convert to UserModel object
        Log.i("AMR", "Length: " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                UsersJson = jsonArray.getJSONObject(i);
                //   Log.i("AMR", "Object" + i +" " + UsersJson);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            UserDataModel Model = UserDataModel.fromJson(UsersJson);
            // Log.i("AMR","MODEL B: "+Model);
            // Log.i("AMR", "PIC" + i +" " + Model.getPicLink());

            // Log.i("AMR","Iteration: "+i);
            if (Model != null) {
                //   Log.i("AMR","ALL: "+AllUsers);
                //  AllUsers.add(Model);
                AllUsers.add(i, Model);
                Log.i("AMR", "ModelName: " + Model.getName());
                Log.i("AMR", "ModelID : " + Model.getID());
                Log.i("AMR", "ModelPic : " + Model.getPicLink());
            }

        }

        return AllUsers;
    }

    public String getName() {
        return User_Name;
    }

    public String getPicLink() {
        return Picture_Url;
    }

    public String getID() {
        return User_ID;
    }

}