package com.example.geeksreads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDataModel {

    String Picture_Url;
    String User_Name;
    String User_ID;

    /**
     * fromJson: Put Data from a single JSONOBJECT into a UserDataModel
     *
     * @param jsonObject
     * @return Single User Prototype
     */
    public static UserDataModel fromJson(JSONObject jsonObject) {
        UserDataModel DummyUser = new UserDataModel();
        // Deserialize json into object fields
        try {
            DummyUser.User_ID = jsonObject.getString("UserId");
            DummyUser.Picture_Url = jsonObject.getString("photo");
            DummyUser.User_Name = jsonObject.getString("UserName");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return DummyUser;
    }

    /**
     * fromJson: Function Takes jsonArray and divides it into
     * several json objects to be put in the form of UserDataModel
     * @param jsonArray
     * @return ArrayList of Users Prototypes
     */
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

            if (Model != null) {
                AllUsers.add(i, Model);
            }

        }

        return AllUsers;
    }

    /**
     * getName: Gets Name of Current DataModelUser (Follower or Following)
     * @return User_Name
     */
    public String getName() {
        return User_Name;
    }

    /**
     * getPicLink: Gets Profile Picture Url of Current DataModelUser (Follower or Following)
     * @return Picture_Url
     */
    public String getPicLink() {
        return Picture_Url;
    }

    /**
     * getID: Gets ID of Current DataModelUser (Follower or Following)
     * @return User_ID
     */
    public String getID() {
        return User_ID;
    }

}