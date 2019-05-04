package com.example.geeksreads;

public class SearchHandlerItem {
    private String bookName;
    private String bookAuthor;
    private String bookID;


    public SearchHandlerItem(String bookName, String bookAuthor,String bookID) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookID=bookID;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookID() {
        return bookID;
    }
}
