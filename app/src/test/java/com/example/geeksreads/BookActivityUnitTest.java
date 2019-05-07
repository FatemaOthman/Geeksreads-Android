package com.example.geeksreads;


import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BookActivityUnitTest {

    @Test
    public void TestReviewNotValid()
    {
        assertEquals(false, BookActivity.checkReview("1234"));
    }

    @Test
    public void TestReviewValid()
    {
        assertEquals(true, BookActivity.checkReview("This is a Review"));
    }

    @Test
    public void TestReviewEmpty()
    {
        assertEquals(false, BookActivity.checkReview(""));
    }
}