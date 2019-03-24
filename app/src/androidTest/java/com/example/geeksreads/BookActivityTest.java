package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookActivityTest {
    @Rule
    public ActivityTestRule<BookActivity> menuActivityTestRule =
            new ActivityTestRule<>(BookActivity.class, true, true);

    @Test
    public void Test() {

        /* Testing getting Author of the Book right */
        assertEquals("By: Jane Austen",BookActivity.sForTestAuthor);

        /* Testing getting Title of the Book right */
        assertEquals("Pride and Prejudice",BookActivity.sForTestTitle);

        /* Testing getting Rating of the Book right */
        assertEquals("4.25",BookActivity.sForTestRate);

        /* Testing getting Date of the Book right */
        assertEquals("Originally Published  28 - 1 - 1813",BookActivity.sForTestDate);

        /* Testing The finishing of all Async Tasks */
        assertEquals("Done",BookActivity.sForTestBookActivity);
    }
}