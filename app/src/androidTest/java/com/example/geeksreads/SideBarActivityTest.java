package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SideBarActivityTest {
    @Rule
    public ActivityTestRule<SideBarActivity> menuActivityTestRule =
            new ActivityTestRule<SideBarActivity>(SideBarActivity.class);
    private SideBarActivity mActivity = null;



    @Before
    public void setUp() throws Exception {
        mActivity=menuActivityTestRule.getActivity();
    }

    @Test
    public void Test() {

        /* Testing getting Number of Followers right */
        assertEquals("14",SideBarActivity.forTestFollowersCount);

        /* Testing getting Number of Books  right */
        assertEquals("25",SideBarActivity.forTestBooksCount);

        /* Testing getting User Name of the User right */
        assertEquals("Fatema Othman",SideBarActivity.forTestUserName);


        /* Testing getting URL of the User's Profile Pic right */
        assertEquals("https://geeksreads.000webhostapp.com/Fatema/userPic.jpg",SideBarActivity.forTestUserPhotoURL);

          }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}