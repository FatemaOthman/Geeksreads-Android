package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import CustomFunctions.APIs;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

public class AuthorActivityTest {
    @Rule
    public ActivityTestRule<AuthorActivity> menuActivityTestRule =
            new ActivityTestRule<>(AuthorActivity.class, true, true);


    @Test
    public void TestViews() {
        if (APIs.MimicModeEnabled) {

            Intent mIntent = new Intent();
            mIntent.putExtra("AuthorID", "11");

            menuActivityTestRule.launchActivity(mIntent);


            /* Testing getting Name of The Author  right */
            onView(withId(R.id.AuthorName)).check(ViewAssertions.matches(withText("Jane Austen")));

            /* Testing getting Number of Books right */
            onView(withId(R.id.NumberOfBooks)).check(ViewAssertions.matches(withText("1")));

            /* Testing getting PhotoURL of the Author right */
            assertEquals("http://geeksreads.000webhostapp.com/Shrouk/Cover.jpg", AuthorActivity.sForTestAuthorPicURL);


        }
    }



}