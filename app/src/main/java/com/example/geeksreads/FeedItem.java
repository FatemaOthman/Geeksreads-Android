package com.example.geeksreads;

public class FeedItem {
    private String postBody;
    private String postTime;
    private String ReviewMakerID;
    private String CommentMakerID;
    private String StatusType;
    private String ReviewID;
    private String CommentID;
    private String FeedType;
    private String BookName;
    private String BookStatue;
    private String BookID;
    private String MakerName;
    private String makerPhoto;

    public FeedItem(String postBody, String postTime, String reviewMakerID, String commentMakerID, String statusType, String reviewID, String commentID, String reviewMakerName, String commentMakerName, String feedType, String bookName, String bookStatue, String bookID, String makerPhoto,String MakerName) {
        this.postBody = postBody;
        this.postTime = postTime;
        ReviewMakerID = reviewMakerID;
        this.CommentMakerID = commentMakerID;
        this.StatusType = statusType;
        this.ReviewID = reviewID;
        this.CommentID = commentID;
        this.FeedType = feedType;
        this.BookName = bookName;
        this.BookStatue = bookStatue;
        this.BookID = bookID;
        this.makerPhoto = makerPhoto;
        this.MakerName=MakerName;
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


    public String getFeedType() {
        return FeedType;
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

    public String getMakerPhoto() {
        return makerPhoto;
    }

    public String getMakerName() {
        return MakerName;
    }
}


