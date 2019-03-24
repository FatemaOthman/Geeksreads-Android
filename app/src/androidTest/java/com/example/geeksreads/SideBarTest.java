
package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SideBarTest{
    public ActivityTestRule<SideBarActivity> menuActivityTestRule =
            new ActivityTestRule<>(SideBarActivity.class, true, true);

    @Test
    public void Test() {

        // forTestUserPhotoURL,forTestUserName,forTestFollowersCount,forTestBooksCount;
            /* Testing getting  User Name right */

            assertEquals("Fatema Othman", SideBarActivity.forTestUserName);

            /* Testing getting The Profile picture URL right */
            assertEquals("http://geeksreads.000webhostapp.com/Fatema/SideBar.php", SideBarActivity.forTestUserPhotoURL);

            /* Testing getting Number of Books  right */
            assertEquals("25", SideBarActivity.forTestBooksCount);

            /* Testing getting Number of Followers right */
            assertEquals("14", SideBarActivity.forTestFollowersCount);

            /* Testing The finishing of all Async Tasks */
            assertEquals("Done", SideBarActivity.forTestSideBarActivity);
        }




}
