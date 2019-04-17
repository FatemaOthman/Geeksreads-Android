package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SideBarActivityTest {
    @Rule
    public ActivityTestRule<SideBarActivity> menuActivityTestRule =
            new ActivityTestRule<SideBarActivity>(SideBarActivity.class);




    @Test
    public void Test() {




        /* Testing getting User Name of the User right */

        onView(withId(R.id.UserNameTxt)).check(ViewAssertions.matches(withText("Mahmoud Morsy")));

        /* Testing getting Number of Followers right */
        assertEquals("4", SideBarActivity.forTestFollowersCount);


        /* Testing getting Number of Books  right */
        assertEquals("94", SideBarActivity.forTestBooksCount);

        /* Testing getting URL of the User's Profile Pic right */
        assertEquals("https://geeksreads.000webhostapp.com/LoggedInUser/PP.jpg", SideBarActivity.forTestUserPhotoURL);


    }



}