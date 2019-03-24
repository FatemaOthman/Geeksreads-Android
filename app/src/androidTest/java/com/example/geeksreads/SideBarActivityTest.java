package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SideBarActivityTest {
    // forTestUserPhotoURL,forTestUserName,forTestFollowersCount,forTestBooksCount,forTestSideBarActivity;
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

        /* Testing getting Rating of the Book right */
        assertEquals("14",SideBarActivity.forTestFollowersCount);

        /* Testing getting Date of the Book right */
        assertEquals("25",SideBarActivity.forTestBooksCount);

        /* Testing getting Title of the Book right */
        assertEquals("Fatema Othman",SideBarActivity.forTestUserName);


        /* Testing getting Author of the Book right */
        assertEquals("https://geeksreads.000webhostapp.com/Fatema/userPic.php",SideBarActivity.forTestUserPhotoURL);



        /* Testing The finishing of all Async Tasks */
        assertEquals("Done",SideBarActivity.forTestSideBarActivity);
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}