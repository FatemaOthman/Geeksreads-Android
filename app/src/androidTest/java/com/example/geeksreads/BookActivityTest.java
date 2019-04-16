package com.example.geeksreads;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class BookActivityTest {
    @Rule
    public ActivityTestRule<BookActivity> menuActivityTestRule =
            new ActivityTestRule<>(BookActivity.class, true, true);

    @Test
    public void TestView() {

        /* Testing getting Author of the Book right */
        onView(withId(R.id.AuthorNameTxt)).check(ViewAssertions.matches(withText("By: Jane Austen")));

        /* Testing getting Title of the Book right */
        onView(withId(R.id.BookTitleTxt)).check(ViewAssertions.matches(withText("Pride and Prejudice")));

        /* Testing getting Rating of the Book right */
        onView(withId(R.id.RatingBar)).check(ViewAssertions.matches(withText("4.25")));
        onView(withId(R.id.ReviewsNumberTxt)).check(ViewAssertions.matches(withText("56204 Reviews")));
        onView(withId(R.id.RatingsNumberTxt)).check(ViewAssertions.matches(withText("2533019 Ratings")));


        /* Testing getting Date of the Book right */
        onView(withId(R.id.PublishedOnTxt)).check(ViewAssertions.matches(withText("Published On  28-1-1813, By: Modern Library")));

        /* Testing getting ISBN of the Book right */
        onView(withId(R.id.ISBN)).check(ViewAssertions.matches(withText("ISBN: 9780679783268")));

        /* Testing getting pages Number of the Book right */
        onView(withId(R.id.pages)).check(ViewAssertions.matches(withText("279 pages")));

        /* Testing The finishing of all Async Tasks */
        assertEquals("Done", BookActivity.sForTestBookActivity);
    }

    @Test
    public void TestButton()
    {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ChooseShelfActivity.class.getName(), null, false);

        onView(withId(R.id.OptionsDropList)).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        // next activity is opened and captured.
        assertNotNull(nextActivity);
        nextActivity .finish();
    }
}