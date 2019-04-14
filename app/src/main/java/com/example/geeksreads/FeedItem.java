package com.example.geeksreads;

import android.widget.ImageView;

public class FeedItem {
    private String postBody;
    private String postTime;
    private ImageView notifierPic;


    private String notifierPicURL;

    public FeedItem(String postBody, String postTime, String notifierPicURL) {
        this.postBody = postBody;
        this.postTime = postTime;
        this.notifierPicURL = notifierPicURL;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getPostTime() {
        return postTime;
    }

    public ImageView getNotifierPic() {
        return notifierPic;
    }
    public String getNotifierPicURL() {
        return notifierPicURL;
    }
}


