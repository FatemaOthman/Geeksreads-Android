package com.example.geeksreads;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class ChooseShelfActivityTest {

    @Rule
    public ActivityTestRule<ChooseShelfActivity> menuActivityTestRule =
            new ActivityTestRule<>(ChooseShelfActivity.class, true, true);

    @Test
    public void TestView()
    {
        /* Testing getting Author of the Book right */
        //onView(withId(R.id.ByAuthorNameTxt)).check(ViewAssertions.matches(withText("By: Jane Austen")));

        /* Testing getting Title of the Book right */
        //onView(withId(R.id.BookNameTxt)).check(ViewAssertions.matches(withText("Pride and Prejudice")));

        /* Testing getting Rating of the Book right */
        //onView(withId(R.id.ratingBar)).check(ViewAssertions.matches(withText("4.25")));
        //onView(withId(R.id.BookRatingsTxt)).check(ViewAssertions.matches(withText("2533019 Ratings")));

        /* Testing getting Date of the Book right */
        //onView(withId(R.id.PublishData)).check(ViewAssertions.matches(withText("Published On  28-1-1813, By: Modern Library")));

        /* Testing getting pages Number of the Book right */
        //onView(withId(R.id.pageNumbers)).check(ViewAssertions.matches(withText("279 pages")));
    }
}