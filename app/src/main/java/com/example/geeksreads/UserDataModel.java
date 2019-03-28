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

/*
    public UserDataModel(String ID ,String Picture ,String Name ) {

        this.User_ID=ID;
        this.Picture_Url=Picture;
        this.User_Name=Name;


    }
*/

    public static UserDataModel fromJson(JSONObject jsonObject) {
        UserDataModel DummyUser = new UserDataModel();
        // Deserialize json into object fields
        try {
            DummyUser.User_ID = jsonObject.getString("id");
            DummyUser.Picture_Url = jsonObject.getString("photourl");
            DummyUser.User_Name = jsonObject.getString("name");
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
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                UsersJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            UserDataModel Model = UserDataModel.fromJson(UsersJson);
            Log.i("AMR", "Object" + i + Model.getPicLink());
            if (Model != null) {
                AllUsers.add(Model);
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