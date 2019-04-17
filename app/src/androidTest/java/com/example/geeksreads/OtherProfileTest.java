package com.example.geeksreads;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class OtherProfileTest {
    @Rule
    public ActivityTestRule<OtherProfileActivity> OtherProfileTestRule =
            new ActivityTestRule<>(OtherProfileActivity.class, true, true);

    @Test
    public void Test_Return_Amr() {

        /* Testing getting Author of the Book right */
        onView(withId(R.id.FollowersList))
                .perform(click());
        assertEquals("Amr", OtherProfileActivity.aForTestUserName);

        /* Testing getting Title of the Book right */
        assertEquals("5", OtherProfileActivity.aForTestBooksCount);

    }


    @Test
    public void Test_Return_Fatema() {

        /* Testing getting User Name right*/
        assertEquals("Fatema", OtherProfileActivity.aForTestUserName);

        /* Testing getting number of Books right */
        assertEquals("5", OtherProfileActivity.aForTestBooksCount);

    }
}