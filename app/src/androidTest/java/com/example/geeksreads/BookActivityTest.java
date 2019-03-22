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
        assertEquals("Done",BookActivity.bookActivityTest);
    }
}