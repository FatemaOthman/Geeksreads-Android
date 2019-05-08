package com.example.geeksreads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static org.json.JSONObject.NULL;

class ReviewDataModel {

    private String ReviewID;
    private String BookCoverPicture;
    private String UserProfilePicture;
    private String UserName;
    private String NLikes;
    private String NComments;
    private String ReviewText;
    private String UserWhoWroteID;
    private String IsLiked;
    private String DateOfReview;
    private String Rating;
    private String BookID;

    /**
     * fromJson: Put Data from a single JSONOBJECT into a UserDataModel
     *
     * @param jsonObject
     * @return Single User Prototype
     */
    private static ReviewDataModel fromJson(JSONObject jsonObject) {
        ReviewDataModel DummyUser = new ReviewDataModel();
        // Deserialize json into object fields
        try {

            DummyUser.BookCoverPicture = jsonObject.getString("bookCover");
            DummyUser.UserProfilePicture = jsonObject.getString("photo");
            DummyUser.UserName = jsonObject.getString("userName");
            DummyUser.BookID = jsonObject.getString("bookId");
            DummyUser.NLikes = jsonObject.getString("likesCount");
            DummyUser.NComments = jsonObject.getString("commCount");
            DummyUser.ReviewText = jsonObject.getString("reviewBody");
            DummyUser.ReviewID = jsonObject.getString("reviewId");
            DummyUser.UserWhoWroteID = jsonObject.getString("userId");
            DummyUser.IsLiked = jsonObject.getString("liked");
            DummyUser.DateOfReview = jsonObject.getString("reviewDate");
            DummyUser.Rating = jsonObject.getString("rating");
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                if (jsonObject.getString("bookCover").equals(NULL)) {
                    DummyUser.BookCoverPicture = jsonObject.getString("photo");

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            // return null;
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
    static ArrayList<ReviewDataModel> fromJson(JSONArray jsonArray) {
        JSONObject UsersJson;
        ArrayList<ReviewDataModel> AllUsers = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to UserModel object
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                UsersJson = jsonArray.getJSONObject(i);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            ReviewDataModel Model = ReviewDataModel.fromJson(UsersJson);

            if (Model != null) {
                AllUsers.add(i, Model);
            }

        }

        return AllUsers;
    }

    /**
     * getName: Gets Name of Current DataModelUser (Follower or Following)
     *
     * @return User_Name
     */
    String getBookCoverPicture() {
        return BookCoverPicture;
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
     * getNLikes: Gets Number of Likes of Current Review
     *
     * @return NLikes
     */
    String getNLikes() {
        return NLikes;
    }

    /**
     * getNComments: Gets Number of comments of Current Review
     *
     * @return NComments
     */
    String getNComments() {
        return NComments;
    }

    /**
     * getReviewText: Gets Text of Current Review
     *
     * @return ReviewText
     */
    String getReviewText() {
        return ReviewText;
    }

    /**
     * getUserName: Gets Text of Current Review
     *
     * @return ReviewText
     */
    String getUserName() {
        return UserName;
    }

    /**
     * getReviewID: Gets Id of current Review
     *
     * @return ReviewID
     */
    String getReviewID() {
        return ReviewID;
    }


    /**
     * getLikeStatus: Gets Like Status of current Review
     *
     * @return IsLiked
     */
    String getLikeStatus() {
        return IsLiked;
    }


    /**
     * getUserwhoWroteID: Gets Id of user who wrote the review
     *
     * @return UserwhoWroteID
     */
    String getUserWhoWroteID() {
        return UserWhoWroteID;
    }

    /**
     * getReviewDate: Gets Date of the review
     *
     * @return DateOfReview
     */
    String getReviewDate() {
        return DateOfReview;
    }

    /**
     * getReviewRating: Gets rating of the review
     *
     * @return rating
     */
    String getReviewRating() {
        return Rating;
    }

    String getBookID() {
        return BookID;
    }

}