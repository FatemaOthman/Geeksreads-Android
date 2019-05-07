package com.example.geeksreads;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedModel {
    public static final int COMMENT_TYPE=0;
    public static final int REVIEW_TYPE=1;

    private String postBody;
    private String postTime;
    private String ReviewMakerID;
    private String CommentMakerID;
    private String StatusType;
    private String ReviewID;
    private String CommentID;
    private String BookName;
    private String BookStatue;
    private String BookID;
    private String CommentMakerName;
    private String ReviewMakerName;
    private String ReviewMakerPhoto;
    private String CommentMakerPhoto;
    private String BookAuthor;
    private String AuthorID;
    private String BookPhoto;
    private String ReviewBody;
    private boolean isLiked;
    public  int type;

    public FeedModel(String postTime, String reviewMakerID, String commentMakerID, String statusType, String reviewID, String commentID, String bookName, String bookStatue, String bookID, String commentMakerName, String reviewMakerName, String reviewMakerPhoto, String commentMakerPhoto, String bookAuthor, String authorID, String bookPhoto, String reviewBody,boolean isLiked) {
        this.postTime = postTime;
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        try {
            Date m = new Date();
           int s =m.getDay();
            Date myDate = myFormat.parse(this.postTime);
            m.getTime();
             s = myDate.getDay();

            this.postTime=myDate.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ReviewMakerID = reviewMakerID;
        CommentMakerID = commentMakerID;
        StatusType = statusType;
        ReviewID = reviewID;
        CommentID = commentID;
        BookName = bookName;
        BookStatue = bookStatue;
        BookID = bookID;
        CommentMakerName = commentMakerName;
        ReviewMakerName = reviewMakerName;
        ReviewMakerPhoto = reviewMakerPhoto;
        CommentMakerPhoto = commentMakerPhoto;
        BookAuthor = bookAuthor;
        AuthorID = authorID;
        BookPhoto = bookPhoto;
        ReviewBody = reviewBody;
        this.isLiked=isLiked;
        switch (StatusType) {
            case "Review":
                type = 1;
                postBody = ReviewMakerName + " reviewd " + BookName;
                break;

            case "Comment":
                type = 0;
                postBody = CommentMakerName + " commented On " + ReviewMakerName + "'s review";
                break;
        }

    }


    public int getType() {
        return type;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getReviewMakerID() {
        return ReviewMakerID;
    }

    public String getCommentMakerID() {
        return CommentMakerID;
    }

    public String getStatusType() {
        return StatusType;
    }

    public String getReviewID() {
        return ReviewID;
    }

    public String getCommentID() {
        return CommentID;
    }


    public String getBookName() {
        return BookName;
    }

    public String getBookStatue() {
        return BookStatue;
    }

    public String getBookID() {
        return BookID;
    }

    public String getCommentMakerName() {
        return CommentMakerName;
    }

    public String getReviewMakerName() {
        return ReviewMakerName;
    }

    public String getReviewMakerPhoto() {
        return ReviewMakerPhoto;
    }

    public String getCommentMakerPhoto() {
        return CommentMakerPhoto;
    }

    public String getBookAuthor() {
        return BookAuthor;
    }

    public String getBookPhoto() {
        return BookPhoto;
    }

    public String getReviewBody() {
        return ReviewBody;
    }

    public String getAuthorID() {
        return AuthorID;
    }

    public boolean isLiked() {
        return isLiked;
    }
    public void setIsLiked(boolean isLiked)
    {
        this.isLiked=isLiked;
    }
}
