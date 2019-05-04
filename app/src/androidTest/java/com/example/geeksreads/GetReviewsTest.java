package com.example.geeksreads;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetReviewsTest {

    @Rule
    public ActivityTestRule<Reviews> menuActivityTestRule =
            new ActivityTestRule<>(Reviews.class, true, false);

    @Test
    public void Test_Reviews() {
        Intent myIntent = new Intent();
        myIntent.putExtra("BookID", "1152003");
        menuActivityTestRule.launchActivity(myIntent);
        assertEquals(Reviews.sForTestReviewsList,
                "[{\"reviewId\":\"1152003\",\"userName\":\"Amr Khaled\",\"BookCover\":\"http:\\/\\/geeksreads.000webhostapp.com\\/Shrouk\\/Cover.jpg\",\"photo\":\"http:\\/\\/geeksreads.000webhostapp.com\\/Fatema\\/userPic.jpg\",\"reviewBody\":\"Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular novels in the English language. Jane Austen called this brilliant work 'her own darling child'and its vivacious heroine, Elizabeth Bennet,'as delightful a creature as ever appeared in print.; The romantic clash between the opinionated Elizabeth and her proud beau, Mr. Darcy, is a splendid performance of civilized sparring. And Jane Austen's radiant wit sparkles as her characters dance a delicate quadrille of flirtation and intrigue, making this book the most superb comedy of manners of Regency England\",\"likesCount\":\"5\",\"commCount\":\"10\",\"userId\":\"1152003\",\"Liked\":\"true\"},{\"reviewId\":\"1142019\",\"userName\":\"Mahmoud Morsy\",\"BookCover\":\"http:\\/\\/geeksreads.000webhostapp.com\\/Shrouk\\/Cover.jpg\",\"photo\":\"http:\\/\\/geeksreads.000webhostapp.com\\/Amr\\/MyPic.jpg\",\"reviewBody\":\"Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular novels in the English language. Jane Austen called this brilliant work 'her own darling child'and its vivacious heroine, Elizabeth Bennet,'as delightful a creature as ever appeared in print.; The romantic clash between the opinionated Elizabeth and her proud beau, Mr. Darcy, is a splendid performance of civilized sparring. And Jane Austen's radiant wit sparkles as her characters dance a delicate quadrille of flirtation and intrigue, making this book the most superb comedy of manners of Regency England\",\"likesCount\":\"15\",\"commCount\":\"20\",\"userId\":\"1142019\",\"Liked\":\"false\"}]");

    }

}