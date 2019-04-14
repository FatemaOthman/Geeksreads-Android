package com.example.geeksreads;


public class BookItem {
    private String bookName;
    private String bookAuthor;
    private String bookRate;
    private String numOfRates;
    private String bookCoverURL;
    private String status="Add to ReadList";

    public BookItem(String bookName, String bookAuthor, String bookRate, String numOfRates, String bookCoverURL, String status) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        //this.bookCover = bookCover;
        //this.bookRateBar = bookRateBar;

        this.bookRate = bookRate;
        this.numOfRates = numOfRates;
        this.bookCoverURL=bookCoverURL;
        this.status=status;

    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }


    public String getBookRate() {
        return bookRate;
    }

    public String getNumOfRates() {
        return numOfRates;
    }

    public String getBookCoverURL() {
        return bookCoverURL;
    }

    public String getStatus() {
        return status;
    }
}