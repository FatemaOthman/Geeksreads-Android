package com.example.geeksreads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentDataModel {

    private String CommentID;
    private String UserProfilePicture;
    private String UserName;
    private String NLikes;
    private String CommentText;
    private String UserWhoWroteID;

    /**
     * fromJson: Put Data from a single JSONOBJECT into a UserDataModel
     *
     * @param jsonObject
     * @return Single User Prototype
     */
    private static CommentDataModel fromJson(JSONObject jsonObject) {
        CommentDataModel DummyUser = new CommentDataModel();
        // Deserialize json into object fields
        try {
            //TODO: Check with backend the missing data!
            DummyUser.UserProfilePicture = jsonObject.getString("UserPicLink");
            DummyUser.UserName = jsonObject.getString("username");
            DummyUser.NLikes = jsonObject.getString("likes");
            DummyUser.CommentText = jsonObject.getString("body");
            DummyUser.CommentID = jsonObject.getString("CommentID");
            DummyUser.UserWhoWroteID = jsonObject.getString("UserWhoWroteID");
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
     *
     * @param jsonArray
     * @return ArrayList of Users Prototypes
     */
    public static ArrayList<CommentDataModel> fromJson(JSONArray jsonArray) {
        JSONObject UsersJson;
        ArrayList<CommentDataModel> AllUsers = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to UserModel object
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                UsersJson = jsonArray.getJSONObject(i);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            CommentDataModel Model = CommentDataModel.fromJson(UsersJson);

            if (Model != null) {
                AllUsers.add(i, Model);
            }

        }

        return AllUsers;
    }

    /**
     * getPicLink: Gets Profile Picture Url of Current DataModelUser (Follower or Following)
     *
     * @return Picture_Url
     */
    String getUserProfilePicture() {
        return UserProfilePicture;
    }

    /**
     * getNLikes: Gets Number of Likes of Current Comment
     *
     * @return NLikes
     */
    String getNLikes() {
        return NLikes;
    }

    /**
     * getCommentText: Gets Text of Current Comment
     *
     * @return CommentText
     */
    String getCommentText() {
        return CommentText;
    }

    /**
     * getUserName: Gets Text of Current Comment
     *
     * @return CommentText
     */
    String getUserName() {
        return UserName;
    }

    /**
     * getCommentID: Gets Id of current Comment
     *
     * @return CommentID
     */
    String getCommentID() {
        return CommentID;
    }


    /**
     * getUserwhoWroteID: Gets Id of user who wrote the Comment
     *
     * @return UserwhoWroteID
     */
    String getUserWhoWroteID() {
        return UserWhoWroteID;
    }

}